const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')


// 모임에서 내보내기
router.post('/deleteMember', (req, res) => {
  console.log('deleteMemberRequest', req.body);
  const { club_code, user_id } = req.body;
  const deleteQuery = `DELETE FROM tb_join WHERE club_code = ? AND user_id = ? ; `
  conn.query(deleteQuery,[club_code, user_id],(err, results) =>{
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