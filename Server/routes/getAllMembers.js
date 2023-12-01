const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

//전체 회원 목록
router.post('/getAllMembers/:club_code', (req, res) => {
  //console.log('request값 확인',req.params);
  const club_code = req.params.club_code; 
  const findMemberQuery = `
      SELECT a.club_code, a.user_id, b.user_name, a.club_role,CONCAT('${config.baseURL}/uploads/', b.user_img) AS user_img , a.join_dt
      FROM tb_join a
      LEFT JOIN tb_user b ON a.user_id = b.user_id
      WHERE a.club_code = ?
      ORDER BY a.club_role ASC, a.join_dt ASC;
  `;
  conn.query(findMemberQuery, [club_code], (err, results) => {
      if (err) {
          return res.status(500).json({ error: err.message });
      }


      if (results.length > 0) {
          //console.log("안드로이드로 보내는 값", results);
          res.status(200).json({ data: results });
      } else {
          res.status(404).json({ error: "Club members not found." });
      }
  });
});

module.exports = router;