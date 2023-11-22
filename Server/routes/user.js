const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const { generateTokens } = require('./token/jwt');
const { normalAuth } = require('./auth'); // 파일 경로는 실제 경로에 맞게 수정해야 합니다.


router.post('/validateToken', normalAuth ,(req, res) => { //normalAuth,
  // console.log("토큰검증하러왔어요 :",req);
  console.log("앱에 보내는 정보 : ",res.locals.userInfo)
  res.json({
      success: true,
      message: 'Token is valid',
      user: res.locals.userInfo
  });
});


// 로그인 기능
router.post('/login', (req, res) => {
  console.log('login router', req.body);
  const { user_id, user_pw } = req.body
  const loginSql = `select * from tb_user where user_id=? and user_pw=?`

  conn.query(loginSql, [user_id, user_pw], (err, rows) => {
    if (err) {
      console.error('로그인 오류:', err);
      return res.status(500).json({ error: 'Internal Server Error' }); // 수정된 부분
    }

    if (rows.length > 0) {
      console.log('로그인 성공');

      // 인증 성공 시 토큰 생성
      const tokens = generateTokens({ userId: user_id });

       // accessToken을 데이터베이스에 저장하는 쿼리
       const updateTokenSql = `UPDATE tb_user SET user_accessToken=? WHERE user_id=?`;

       conn.query(updateTokenSql, [tokens.accessToken, user_id], (err, result) => {
         if (err) {
           console.error('토큰 저장 오류:', err);
           return res.status(500).json({ error: 'Token Storage Error' });
         }
 
         res.json({
           rows: 'success',
           userId: user_id,
           accessToken: tokens.accessToken,
           refreshToken: tokens.refreshToken
         });
         
        console.log("1user_Id",tokens.accessToken);
       });
      
    } else {
      res.json({ rows: 'failed' })
    }
  });
});

function genderFormat(gender) {
  return gender === '남' ? 'M' : 'F'
}

// 회원가입 기능
router.post('/signup', (req, res) => {
  console.log('signup router', req.body);
  const { user_id, user_pw, user_name, gender, birth_year, user_gu, user_dong, user_introduce } = req.body
  const formattedGender = genderFormat(gender);

  const signUpSql = `insert into tb_user (user_id, user_pw, user_name, gender, birth_year, user_gu, user_dong, user_introduce)
    values(?, ?, ?, ?, ?, ?, ?, ?)`

  conn.query(signUpSql, [user_id, user_pw, user_name, formattedGender, birth_year, user_gu, user_dong, user_introduce], (err, rows) => {
    console.log('회원가입 :', rows);
    if (err) {
      console.error('회원가입 실패:', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('회원가입 성공');
      res.json({ rows: 'success', user_id: user_id });
    }
  });
})


// const dupUserIdSql = `select user_id from tb_user where id =?`

//   conn.query(dupUserIdSql, [user_id], (err, rows) => {
//     console.log('아이디 중복체크 :', rows);
//     if (rows.length > 0) {
//       res.json({rows: 'dup'})
//     } else {
      
      
//     }
//   });

module.exports = router;