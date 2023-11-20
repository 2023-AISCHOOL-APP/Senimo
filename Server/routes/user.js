const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const { generateTokens } = require('./token/jwt');

// 로그인 기능
router.post('/login', (req, res) => {
  console.log('login router', req.body);
  let {user_id, user_pw} = req.body
  
  let query = `select * from tb_user where user_id=? and user_pw=?`

  conn.query(query, [user_id, user_pw], (err, rows) => {
    //if (err) throw error;
    console.log('로그인 로직 :', rows);

    if (rows.length > 0) {
      console.log('로그인 성공');
      // 인증 성공 시 토큰 생성
      const tokens = generateTokens({ userId: user_id });
      res.json({rows: 'success', userId: user_id ,accessToken: tokens.accessToken,
      refreshToken: tokens.refreshToken})
      console.log("1userId",tokens);
      console.log("1user_Id",tokens.accessToken);
      
    } else {
      res.json({rows: 'failed'})
    }
  });
});

module.exports = router;