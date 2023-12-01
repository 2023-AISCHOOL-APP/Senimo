const express = require('express');
const router = express.Router();
const conn = require('../config/database');
const config = require('../config/config');
const multer = require('multer');
const { v4: uuidv4 } = require('uuid');


const club_code = uuidv4(); // UUID 생성
// multer 설정
const storage = multer.diskStorage({
   destination: function (req, file, cb) {
      // 파일이 저장될 경로
      cb(null, 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads');
   },
   filename: function (req, file, cb) {
      // 파일 저장시 사용할 이름
      const uniqueFilename = uuidv4() + '-' + file.originalname;
      cb(null, uniqueFilename);
      // console.log("사진이름: ", uniqueFilename);
   }
});

const upload = multer({ storage: storage });

router.post('/postCreateMeeting', upload.single('picture'), (req, res) => {
   // 트랜잭션 시작
   conn.beginTransaction(err => {
      if (err) {
         return res.status(500).json({ error: '트랜잭션 시작 오류: ' + err.message });
      }

      const meetingData = JSON.parse(req.body.meeting);
      const { user_id, club_name, club_introduce, max_cnt, club_location, keyword_name } = meetingData;
      const club_img = req.file ? req.file.filename : null;

      // 0단계 : keywordcode 찾기
      const findKeywordCodeQuery = `SELECT keyword_code FROM tb_keyword WHERE keyword_name = ?;`;
      conn.query(findKeywordCodeQuery, [keyword_name], (err, results) => {
         if (err) {
            return conn.rollback(() => {
               res.status(500).json({ error: '0단계 : keywordcode 찾기 오류: ' + err.message });
            });
         }
         const keyword_code = results[0].keyword_code;

         // 1단계: 모임 생성
         const insertClubQuery = `INSERT INTO tb_club (user_id, club_name, club_introduce, max_cnt, club_location, keyword_code, club_img) VALUES (?, ?, ?, ?, ?, ?, ?);`;
         conn.query(insertClubQuery, [user_id, club_name, club_introduce, max_cnt, club_location, keyword_code, club_img], (err, insertResult) => {
            if (err) {
               return conn.rollback(() => {
                  res.status(500).json({ error: '1단계: 모임 생성 오류: ' + err.message });
               });
            }

				// 2단계: 생성된 클럽의 코드 조회
				const clubCodeQuery = `SELECT club_code FROM tb_club WHERE user_id = ? ORDER BY opened_dt DESC LIMIT 1;`;
				conn.query(clubCodeQuery, [user_id], (err, codeResults) => {
					if (err || codeResults.length === 0) {
						return conn.rollback(() => {
							res.status(500).json({ error: '2단계: 생성된 클럽의 코드 조회 오류: ' + err.message });
						});
					}5

               const club_code = codeResults[0].club_code;

               // 3단계: 모임장으로 클럽 가입
               const insertJoinQuery = `INSERT INTO tb_join (club_code, user_id, club_role) VALUES (?, ?, 1);`;
               conn.query(insertJoinQuery, [club_code, user_id], (err, joinResult) => {
                  if (err) {
                     return conn.rollback(() => {
                        res.status(500).json({ error: '3단계: 모임장으로 클럽 가입 오류: ' + err.message });
                     });
                  }

                  // 4단계: 모임인원수 조회
                  const selectJoinMemberQuery = `SELECT COUNT(user_id) AS joined_members FROM tb_join WHERE club_code = ?;`;
                  conn.query(selectJoinMemberQuery, [club_code], (err, selectResult) => {
                     if (err) {
                        return conn.rollback(() => {
                           res.status(500).json({ error: '4단계: 모임인원수 조회 실패: ' + err.message });
                        });
                     }

                     const attend_user_cnt = selectResult[0].joined_members;

                     // 뱃지 코드 직접 지정
                     const badge_code = 'badge_code08';

							// 이미 해당 뱃지 코드와 사용자 ID의 조합이 tb_user_badge에 있는지 확인
							const checkUserBadgeQuery = `SELECT * FROM tb_user_badge WHERE badge_code = ? AND user_id = ?;`;
							conn.query(checkUserBadgeQuery, [badge_code, user_id], (err, results) => {
								if (err) {
									return conn.rollback(() => {
										res.status(500).json({ error: '뱃지 조회 오류: ' + err.message });
									});
								}

								if (results.length === 0) {
									// tb_user_badge에 조합이 없으면 새로 추가
									const insertUserBadgeQuery = `INSERT INTO tb_user_badge (badge_code, user_id) VALUES (?, ?);`;
									conn.query(insertUserBadgeQuery, [badge_code, user_id], (err, userBadgeResult) => {
										if (err) {
											return conn.rollback(() => {
												res.status(500).json({ error: '뱃지 저장 오류: ' + err.message });
											});
										}

										// 트랜잭션 커밋
										conn.commit(err => {
											if (err) {
												return conn.rollback(() => {
													res.status(500).json({ error: '트랜잭션 커밋 오류: ' + err.message });
												});
											}

											// 성공 응답
											res.json({
												message: '모임이 성공적으로 생성되었습니다.',
												club_code,
												club_name,
												club_introduce,
												max_cnt,
												club_location,
												keyword_name,
												attend_user_cnt,
												club_img: `${config.baseURL}/uploads/${club_img}`
											});
										});
									});
								} else {
									// 이미 해당 조합이 존재하므로 추가하지 않고 메시지 반환
									conn.commit(err => {
										if (err) {
											return conn.rollback(() => {
												res.status(500).json({ error: '트랜잭션 커밋 오류: ' + err.message });
											});
										}

										res.json({
											message: '모임이 성공적으로 생성되었습니다.',
												club_code,
												club_name,
												club_introduce,
												max_cnt,
												club_location,
												keyword_name,
												attend_user_cnt,
												club_img: `${config.baseURL}/uploads/${club_img}`
										});
									});
								}
							});
						});
					});
				});
			});
		});
	});
});



// 모임 가입
router.post('/joinClub', (req, res) => {
   //console.log('joinClub router', req.body);
   const { club_code, user_id } = req.body;

   const joinClubSql = `insert into tb_join (club_code, user_id) values (?,?)`;

   conn.query(joinClubSql, [club_code, user_id], (err, rows) => {
      //console.log('모임 가입 : ', rows);
      if (err) {
         console.error('모임 가입 실패', err);
         res.json({ rows: 'failed' });
      } else {
         console.log('모임 가입 성공');

         // 해당 사용자의 모임 가입 수 조회
         const countUserClubsSql = `select count(*) as clubCount from tb_join where user_id = ?`;

         conn.query(countUserClubsSql, [user_id], (err, result) => {
            if (err) {
               console.error("사용자 모임 가입 수 조회 오류: ", err);
               res.status(500).json({ error: '사용자 모임 가입 수 조회 오류: ' + err.message });
            } else {
               const clubCount = result[0].clubCount;

               // 뱃지 코드 지정
               let badge_code = '';

               if (clubCount === 1 || clubCount === 2) {
                  // 처음 모임에 가입할 때 'badge_code03' 부여
                  badge_code = 'badge_code03';
               } else if (clubCount >= 5) {
                  // 5개 이상의 모임에 가입했을 때 'badge_code04' 부여
                  badge_code = 'badge_code04';
               }

               if (badge_code !== '') {
                  // 이미 해당 뱃지 코드와 사용자 ID의 조합이 tb_user_badge에 있는지 확인
                  const checkUserBadgeQuery = `select * from tb_user_badge where badge_code = ? and user_id = ?;`;

                  conn.query(checkUserBadgeQuery, [badge_code, user_id], (err, results) => {
                     if (err) {
                        console.error("뱃지 조회 오류: ", err);
                        res.status(500).json({ error: '뱃지 조회 오류: ' + err.message });
                     } else {
                        if (results.length === 0) {
                           // tb_user_badge에 조합이 없으면 새로 추가
                           const insertUserBadgeQuery = `insert into tb_user_badge (badge_code, user_id) values (?, ?);`;

                           conn.query(insertUserBadgeQuery, [badge_code, user_id], (err, userBadgeResult) => {
                              if (err) {
                                 console.error("뱃지 저장 실패: ", err);
                                 res.status(500).json({ error: '뱃지 저장 오류: ' + err.message });
                              } else {
                                 res.json({ rows: 'success' });
                                 console.log(`뱃지 (${badge_code}) 저장 성공`);
                              }
                           });
                        } else {
                           // 이미 해당 조합이 존재하므로 추가하지 않고 메시지 반환
                           res.json({ rows: 'success' });
                           console.log(`이미 해당 뱃지 코드 (${badge_code})와 사용자 ID의 조합이 존재합니다.`);
                        }
                     }
                  });
               } else {
                  res.json({ rows: 'success' });
                  console.log("해당 조건에 맞는 뱃지가 없습니다.");
               }
            }
         });
      }
   });
});

// 모임 탈퇴
router.post('/quitClub', (req, res) => {
   //console.log('quitClub router', req.body);
   const { club_code, user_id } = req.body

   const joinClubSql = `delete from tb_join where club_code = ? and user_id = ?`

   conn.query(joinClubSql, [club_code, user_id], (err, rows) => {
      console.log('모임 탈퇴 : ', rows);
      if (err) {
         console.error('모임 탈퇴 실패', err);
         res.json({ rows: 'failed' });
      } else {
         console.log('모임 탈퇴 성공');
         res.json({ rows: 'success' });
      }
   })
})

module.exports = router;