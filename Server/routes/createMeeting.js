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
        console.log("사진이름: ", uniqueFilename);
    }
});

const upload = multer({ storage: storage });

// 모임 정보 생성 및 사진 업로드 라우트
router.post('/postCreateMeeting', upload.single('picture'), (req, res) => {
        // 트랜잭션 시작
        conn.beginTransaction(err => {
            if (err) {
                return res.status(500).json({ error: '트랜잭션 시작 오류: ' + err.message });
                console.log("트랜잭션 시작 오류",err.message);
            }

            // JSON 문자열을 객체로 변환
            const meetingData = JSON.parse(req.body.meeting);
            const { user_id, club_name, club_introduce, max_cnt, club_location, keyword_name } = meetingData;
            const club_img = req.file ? req.file.filename : null;

            // 0단계 : keywordcode 찾기
            const findKeywordCodeQuery = `SELECT keyword_code FROM tb_keyword WHERE keyword_name = ?;`;
        conn.query(findKeywordCodeQuery, [keyword_name], (err, results) => {
            if (err) {
                return res.status(500).json({ error: err.message });
                console.log("0단계 : keywordcode 찾기",err.message);
            }
            const keyword_code = results[0].keyword_code;
            // 1단계: 모임 생성
            const insertClubQuery = `
                INSERT INTO tb_club (user_id, club_name, club_introduce, max_cnt, club_location, keyword_code, club_img) 
                VALUES (?, ?, ?, ?, ?, ?, ?);
            `;
            conn.query(insertClubQuery, [user_id, club_name, club_introduce, max_cnt, club_location, keyword_code, club_img], (err, insertResult) => {
                if (err) {
                    return conn.rollback(() => {
                        res.status(500).json({ error: '모임 생성 오류: ' + err.message });
                        console.log("1단계: 모임 생성",err.message);
                    });
                }

                // 2단계: 생성된 클럽의 코드 조회
                const clubCodeQuery = `
                    SELECT club_code FROM tb_club WHERE user_id = ? ORDER BY opened_dt DESC LIMIT 1;
                `;
                conn.query(clubCodeQuery, [user_id], (err, codeResults) => {
                    if (err || codeResults.length === 0) {
                        return conn.rollback(() => {
                            res.status(500).json({ error: '클럽 코드 조회 오류: ' + err.message });
                            console.log("2단계: 생성된 클럽의 코드 조회",err.message);
                        });
                    }

                    const club_code = codeResults[0].club_code;

                    // 3단계: 모임장으로 클럽 가입
                    const insertJoinQuery = `
                        INSERT INTO tb_join (club_code, user_id, club_role) VALUES (?, ?, 1);
                    `;
                    conn.query(insertJoinQuery, [club_code, user_id], (err, joinResult) => {
                        if (err) {
                            return conn.rollback(() => {
                                res.status(500).json({ error: '모임장 가입 오류: ' + err.message });
                                console.log(" 3단계: 모임장으로 클럽 가입",err.message);
                            });
                        }

                        // 트랜잭션 커밋
                        conn.commit(err => {
                            if (err) {
                                return conn.rollback(() => {
                                    res.status(500).json({ error: '트랜잭션 커밋 오류: ' + err.message });
                                    console.log(" 트랜잭션 커밋",err.message);
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
                                club_img: `${config.baseURL}/uploads/${club_img}`
                            });
                            console.log("성공적으로 모임을 생성하였습니다.",res)
                        });
                    });
                });
            });
        });
    });
});


// 모임 가입
router.post('/joinClub', (req, res) => {
	console.log('joinClub router', req.body);
	const { club_code, user_id } = req.body

	const joinClubSql = `insert into tb_join (club_code, user_id) values (?,?)`

	conn.query(joinClubSql, [club_code, user_id], (err, rows) => {
		console.log('모임 가입 : ', rows);
		if (err) {
			console.error('모임 가입 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('모임 가입 성공');
      res.json({ rows: 'success'});
    }
	})
})

// 모임 탈퇴
router.post('/quitClub', (req, res) => {
	console.log('quitClub router', req.body);
	const { club_code, user_id } = req.body

	const joinClubSql = `delete from tb_join where club_code = ? and user_id = ?`

	conn.query(joinClubSql, [club_code, user_id], (err, rows) => {
		console.log('모임 탈퇴 : ', rows);
		if (err) {
			console.error('모임 탈퇴 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('모임 탈퇴 성공');
      res.json({ rows: 'success'});
    }
	})
})

module.exports = router;