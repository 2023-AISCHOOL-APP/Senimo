const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

// 일정 참가 회원 목록
router.get('/getScheduleMembers/:sche_code', (req, res) => {
  console.log('request값 확인',req.params);
  const sche_code = req.params.sche_code; 
  const findMemberQuery = `
  SELECT u.user_id, u.user_name, j.club_role ,u.user_img
  FROM tb_sche_joined_user sju
  JOIN tb_user u ON sju.user_id = u.user_id
  JOIN tb_schedule sch ON sju.sche_code = sch.sche_code
  JOIN tb_join j ON sju.user_id = j.user_id AND sch.club_code = j.club_code
  WHERE sju.sche_code = ? ;
  `;
  conn.query(findMemberQuery, [sche_code], (err, results) => {
      if (err) {
          return res.status(500).json({ error: err.message });
      }

      if (results.length > 0) {
          console.log("일정 참가 목록", results);
          res.status(200).json({ data: results });
      } else {
          res.status(404).json({ error: "Schedule members not found." });
      }
  });
});

module.exports = router;