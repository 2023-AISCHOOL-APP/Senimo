const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

// 일정 정보 조회
router.get('/get/Sche_intro/:sche_code', (req, res) => {
  console.log('result', req.body);
  // 클라이언트로부터 받은 schedule_id를 사용하여 쿼리문을 생성
  const sche_code = req.params.sche_code;
  console.log("요청", req)
  console.log("요청받은코드", sche_code)
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
    console.log("보낸결과", results)
  });
});

// 일정 생성
router.post('/makeSche', (req, res) => {
  console.log('makeSche router', req.body);
  const { sche_code, club_code, sche_title, sche_content, sche_date, sche_location, max_num, fee, sche_img } = req.body
  const formattedDate = new Date(sche_date)

  const makeScheSql = `insert into tb_schedule (club_code, sche_title, sche_content, sche_date, sche_location, max_num, fee, sche_img)
    values(?,?,?,?,?,?,?,?)`
  conn.query(makeScheSql, [club_code, sche_title, sche_content, formattedDate, sche_location, max_num, fee, sche_img], (err, rows) => {
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

// 일정 참가
router.post('/joinSche', (req, res) => {
  console.log('joinSche router', req.body);
  const { user_id, sche_code } = req.body

  const joinScheSql = `insert into tb_sche_joined_user (user_id, sche_code) values (?,?)`

  conn.query(joinScheSql, [user_id, sche_code], (err, rows) => {
    console.log('일정 참가 : ', rows);
    if (err) {
      console.error('일정 참가 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('일정 참가 성공');
      res.json({ rows: 'success'});
    }
  });
})

// 일정 탈퇴
router.post('/cancelJoinSche', (req, res) => {
  console.log('cancelJoinSche router', req.body);
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
  console.log('일정 삭제 라우터', req.body);
  const { sche_code } = req.body;

  const deletePostSql = `delete from tb_schedule where sche_code = ?`

  conn.query(deletePostSql, [ sche_code ], (err, rows) => {
    if (err) {
      console.error('일정 삭제 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('일정 삭제 성공');
      res.json({ rows: 'success' });
    }
  })
})

module.exports = router;