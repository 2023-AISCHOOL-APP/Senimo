const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const { generateTokens } = require('../token/jwt');

// 로그인 기능
router.post('/login', (req, res) => {
  console.log('login router', req.body);
  const { user_id, user_pw } = req.body
  const loginSql = `select * from tb_user where user_id=? and user_pw=?`

  conn.query(loginSql, [user_id, user_pw], (err, rows) => {
    if (err) throw error;
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
      res.json({ rows: 'failed' })
      console.error('회원가입 실패:', err);
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

// 아이디 중복체크
router.post('/checkUserId', (req, res) => {
  console.log('checkUserId router', req.body);
  const { user_id } = req.body
  const checkUserIdSql = `select user_id from tb_user where user_id =?`

  conn.query(checkUserIdSql, [user_id], (err, rows) => {
    console.log('아이디 중복체크 :', rows);
    if (err) {
      console.error('아이디 중복 체크 에러:', err);
      res.json({ rows: 'error' });
    } else {
      if (rows.length > 0) {
        // 데이터가 존재하는 경우 - 중복
        console.log('아이디 중복:', rows);
        res.json({ rows: 'dup' });
      } else {
        // 데이터가 존재하지 않는 경우 - 사용 가능
        console.log('아이디 사용 가능');
        res.json({ rows: 'success', user_id: user_id });
      }
    }
  });
})

module.exports = router;