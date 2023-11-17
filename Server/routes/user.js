const express = require('express')
const router = express.Router()
const conn = require('../config/database');

// 로그인 기능
router.post('/login', (req, res) => {
  console.log('login router', req.body);
  let {user_id, user_pw} = req.body
  
  let query = `select * from tb_user where user_id=? and user_pw=?`

  conn.query(query, [user_id, user_pw], (err, rows) => {
    console.log('로그인 로직 :', rows);

    if (rows.length > 0) {
      console.log('로그인 성공');
      res.json({rows: 'success', user_id: user_id })
    } else {
      res.json({rows: 'failed'})
    }
  });
});

module.exports = router;