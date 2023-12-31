const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')
const fs = require('fs');
const multer = require('multer');
const path = require('path');
const { v4: uuidv4 } = require('uuid');
const { UPLOADS_PATH } = require('../config/config');

// multer 설정
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    // 파일이 저장될 경로
    cb(null, `${UPLOADS_PATH}`);
  },
  filename: function (req, file, cb) {
    // 파일 저장시 사용할 이름
    const uniqueFilename = uuidv4() + '-' + file.originalname;
    cb(null, uniqueFilename);
    // console.log("사진이름: ", uniqueFilename);
  }
});

const upload = multer({ storage: storage });

// 일정 정보 조회
router.get('/get/Sche_intro/:sche_code', (req, res) => {
  // console.log('result', req.body);
  // 클라이언트로부터 받은 schedule_id를 사용하여 쿼리문을 생성
  const sche_code = req.params.sche_code;
  // console.log("요청", req)
  // console.log("요청받은코드", sche_code)
  const query = `SELECT c.club_name, CONCAT('${config.baseURL}/uploads/', a.sche_img) AS sche_img , a.sche_title, a.sche_date, a.sche_location, a.fee, 
    COUNT(b.user_id) AS attend_user_cnt, a.max_num, a.sche_content
  FROM tb_schedule a
  LEFT JOIN tb_sche_joined_user b ON a.sche_code = b.sche_code
  left join tb_club c on a.club_code = c.club_code
  WHERE a.sche_code = ?
  GROUP BY a.sche_img, a.sche_title, a.sche_date, a.sche_location, a.fee, a.max_num;
  ;
`;

  conn.query(query, [sche_code], (error, results) => {
    if (error) {
      return res.status(500).send('Server error occurred: ' + error.message);
    }
    res.json(results[0]); // 만약 결과가 하나만 예상된다면 첫 번째 결과만 반환
    // console.log("보낸결과", results)
  });
});

// 일정 생성
router.post('/makeSche', upload.single('picture'), (req, res) => {
  // console.log('makeSche router', req.body);
  const makeSche = JSON.parse(req.body.makeSche)
  const { sche_code, club_code, sche_title, sche_content, sche_date, sche_location, max_num, fee, joined_Members, sche_img } = makeSche
  const formattedDate = new Date(sche_date)

  // 파일이 업로드된 경우, 파일명을 사용
  const scheImgFilename = req.file ? req.file.filename : null;

  const makeScheSql = `insert into tb_schedule (club_code, sche_title, sche_content, sche_date, sche_location, max_num, fee, sche_img)
    values(?,?,?,?,?,?,?,?)`
  conn.query(makeScheSql, [club_code, sche_title, sche_content, formattedDate, sche_location, max_num, fee, scheImgFilename], (err, rows) => {
    // console.log('일정 생성 : ', rows);
    if (err) {
      console.error('일정 생성 실패 : ', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('일정등록 성공');
      res.json({ rows: 'success' });
    }
  });
})

// 일정 수정
router.post('/updateSche', upload.single('picture'), (req, res) => {
  const updateSche = JSON.parse(req.body.updateSche)
  const { sche_code, club_code, sche_title, sche_content, sche_date, sche_location, max_num, fee, joined_Members, sche_img, imageChanged } = updateSche
  const formattedDate = new Date(sche_date)

  const getOldImagePathQuery = `select sche_img from tb_schedule where sche_code = ?`;
  conn.query(getOldImagePathQuery, [sche_code], (err, results) => {
    if (err) {
      return res.status(500).send('Database error: ' + err.message);
    }
    oldImagePath = results[0]?.post_img;

    let newImagePath;

    // 기존 이미지가 있고, 새 이미지가 업로드된 경우 기존 이미지 삭제
    if (imageChanged && req.file) {
      // 새 이미지 업로드 처리
      newImagePath = req.file.filename;

      // 기존 이미지 삭제
      if (oldImagePath) {
        fs.unlink(path.join(`${UPLOADS_PATH}/${oldImagePath}`), (err) => {
          if (err) console.error('Failed to delete old image:', err);
        });
      }
    } else {
      //기존 이미지 유지
      newImagePath = oldImagePath;
    }

    // 게시글 업데이트 
    // console.log("newImagePath : ", newImagePath)
    const updateQuery = `
    UPDATE tb_schedule
    SET sche_title = ?, sche_content = ?, sche_date = ?, max_num = ?,  fee = ?, sche_img = ?
    WHERE sche_code = ?;`;
    conn.query(updateQuery, [sche_title, sche_content, formattedDate, max_num, fee, newImagePath, sche_code], (err, result) => {
      if (err) {
        // 파일 삭제 로직
        if (req.file) {
          const filePath = `${UPLOADS_PATH}/${req.file.filename}`;
          fs.unlink(filePath, err => {
            if (err) console.error('Failed to delete uploaded file:', err);
          });
        }
        return res.status(500).json({ error: err.message });
        console.log("실패")

      } else {
        res.status(200).json({
          club_img: `${config.baseURL}/uploads/${newImagePath}`
        })
        console.log("수정완료")
      }
    })
  }); //getOldImagePathQueryv
}); //updateSche


// 일정 참가
router.post('/joinSche', (req, res) => {
  // console.log('joinSche router', req.body);
  const { user_id, sche_code } = req.body;

  const joinScheSql = `insert into tb_sche_joined_user (user_id, sche_code) values (?,?)`;

  conn.query(joinScheSql, [user_id, sche_code], (err, rows) => {
    console.log('일정 참가 : ', rows);
    if (err) {
      console.error('일정 참가 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('일정 참가 성공');

      // 해당 사용자의 일정 참가 수 조회
      const countUserScheAttendancesSql = `select count(*) as attendances from tb_sche_joined_user where user_id = ?`;

      conn.query(countUserScheAttendancesSql, [user_id], (err, result) => {
        if (err) {
          console.error("사용자 일정 참가 수 조회 오류: ", err);
          res.status(500).json({ error: '사용자 일정 참가 수 조회 오류: ' + err.message });
        } else {
          const attendances = result[0].attendances;

          // 뱃지 코드 지정
          let badge_code = '';

          if (attendances === 1) {
            badge_code = 'badge_code02';
          } else if (attendances === 5) {
            badge_code = 'badge_code06';
          } else if (attendances === 15) {
            badge_code = 'badge_code07';
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
                  //console.log(`이미 해당 뱃지 코드 (${badge_code})와 사용자 ID의 조합이 존재합니다.`);
                }
              }
            });
          } else {
            res.json({ rows: 'success' });
            //console.log("해당 조건에 맞는 뱃지가 없습니다.");
          }
        }
      });
    }
  });
});



// 일정 탈퇴
router.post('/cancelJoinSche', (req, res) => {
  //console.log('cancelJoinSche router', req.body);
  const { user_id, sche_code } = req.body;

  const cancelJoinScheSql = `delete from tb_sche_joined_user where user_id = ? and sche_code = ?`;

  conn.query(cancelJoinScheSql, [user_id, sche_code], (err, rows) => {
    if (err) {
      console.error('일정 참가 취소 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('일정 참가 취소 성공');
      res.json({ rows: 'success' });
    }
  });
});

// 일정 삭제
router.post('/deleteSche', (req, res) => {
  //console.log('일정 삭제 라우터', req.body);
  const { sche_code } = req.body;

  const deletePostSql = `delete from tb_schedule where sche_code = ?`

  conn.query(deletePostSql, [sche_code], (err, rows) => {
    if (err) {
      console.error('일정 삭제 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('일정 삭제 성공');
      res.json({ rows: 'success' });
    }
  })
})


// 일정 업데이트
router.post('/updateSche', (req, res) => {
  //console.log('updateSche router', req.body);
  const { sche_code, club_code, sche_title, sche_content, sche_date, sche_location, max_num, fee, joined_Members, sche_img, club_name } = req.body
  const formattedDate = new Date(sche_date)

  const updateScheSql = `update tb_schedule
  set sche_title = ?, sche_content = ?, sche_date = ?, sche_location = ?, max_num = ?, fee = ?, sche_img = ?
  where sche_code = ? ;`

  conn.query(updateScheSql, [sche_title, sche_content, formattedDate, sche_location, max_num, fee, sche_img, sche_code], (err, rows) => {
    console.log('일정 생성 : ', rows);
    if (err) {
      console.error('일정 생성 실패 : ', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('일정등록 성공');
      res.json({ rows: 'success' });
    }
  });
})




module.exports = router;