const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.post('/postModifyMeeting', (req, res) => {
  console.log('result', req.body);
  const { club_code, user_id } = req.body;
  const InterestQuery = `SELECT interest_code FROM tb_interested_club WHERE user_id = ? AND club_code = ?;`;
  
  conn.query(InterestQuery, [user_id, club_code], (err, result) => {
      if (err) {
          res.status(500).json({ error: err.message });
          console.log("실패");
      } else {
          if (result.length > 0) {
              // 결과가 존재하는 경우
              const deleteInterestQuery = `DELETE FROM tb_interested_club WHERE user_id = ? AND club_code = ?;`;
              conn.query(deleteInterestQuery, [user_id, club_code], (err, result) => {
                  if (err) {
                      res.status(500).json({ error: err.message });
                      console.log("데이터 삭제 실패");
                  } else {
                      res.status(200).json({ result: false });
                      console.log("관심 모임 삭제됨");
                  }
              });
          } else {
              // 결과가 존재하지 않는 경우
              const insertInterestQuery = `INSERT INTO tb_interested_club (club_code, user_id) VALUES (?, ?);`;
              conn.query(insertInterestQuery, [club_code, user_id], (err, result) => {
                  if (err) {
                      res.status(500).json({ error: err.message });
                      console.log("데이터 추가 실패");
                  } else {
                      res.status(200).json({ result: true });
                      console.log("관심 모임 추가됨");
                  }
              });
          }
      }
  });
});

module.exports = router;