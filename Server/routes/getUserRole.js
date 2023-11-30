const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.post('/getUserRole', (req, res) => {
  //console.log('역할result', req.body);
  const { club_code, user_id } = req.body;
  const RoleQuery = `SELECT club_role FROM tb_join where club_code = ? and user_id =?`;
  
  conn.query(RoleQuery, [club_code, user_id], (err, result) => {
      if (err) {
          res.status(500).json({ error: err.message });
          console.log("실패");
      } else {
          res.status(200).json( {userRole : result[0].club_role});
          console.log(`사용자 역할 조회 성공 ${result[0].club_role}`);
      }
      }
  )}
);


module.exports = router;