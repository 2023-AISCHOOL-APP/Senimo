const express = require('express');
const router = express.Router();
const conn = require('../config/database');
const config = require('../config/config');

router.post('/updateMember', (req, res) => {
  console.log('updateMemberRequest result', req.body);
  const { club_code, user_id, club_role } = req.body;
  const updateQuery = `UPDATE tb_join SET club_role = ? WHERE user_id = ? and club_code = ?;` // 회원 역할 변경
  conn.query(updateQuery,[club_role, user_id, club_code],(err, results) =>{
    if (err) {
      console.error("Error occurred: ", err);
      res.status(500).json({ error: err.message });
    } else {
      if (results.affectedRows === 0) {
        res.status(200).json({ success: true, message: "삭제할 멤버가 없습니다." });
      } else {
        res.status(200).json({ success: true, message: "멤버 삭제 성공." });
      }
      console.log('응답성공: ', results);
    }
  });
  
}); 

module.exports = router;