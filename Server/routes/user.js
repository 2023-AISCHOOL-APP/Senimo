const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const { generateTokens } = require('../token/jwt');
const { normalAuth } = require('./auth'); // 파일 경로는 실제 경로에 맞게 수정해야 합니다.
const bcrypt = require('bcrypt')


router.post('/validateToken', normalAuth, (req, res) => { //normalAuth,
  // console.log("토큰검증하러왔어요 :",req);
  console.log("앱에 보내는 정보 : ", res.locals.userInfo)
  res.json({
    success: true,
    message: 'Token is valid',
    user: res.locals.userInfo
  });
});

// 로그인 기능
router.post('/login', (req, res) => {
  //console.log('login router', req.body);
  const { user_id, user_pw } = req.body;
  const loginSql = `SELECT * FROM tb_user WHERE user_id=?`;

  conn.query(loginSql, [user_id], (err, rows) => {
    if (err) {
      console.error('로그인 오류:', err);
      return res.status(500).json({ error: 'Internal Server Error' });
    }

    if (rows.length > 0) {
      const storedHashedPassword = rows[0].user_pw;

      // bcrypt를 사용하여 입력된 비밀번호와 저장된 해시된 비밀번호를 비교
      bcrypt.compare(user_pw, storedHashedPassword, (err, result) => {
        if (err) {
          console.error('비밀번호 비교 오류:', err);
          return res.status(500).json({ error: 'Password Comparison Error' });
        }

        if (result) {
          // 인증 성공 시 토큰 생성
          const tokens = generateTokens({ userId: user_id });

          // accessToken을 데이터베이스에 저장하는 쿼리
          const updateTokenSql = `UPDATE tb_user SET user_accessToken=? WHERE user_id=?`;

          conn.query(updateTokenSql, [tokens.accessToken, user_id], (err, result) => {
            if (err) {
              console.error('토큰 저장 오류:', err);
              return res.status(500).json({ error: 'Token Storage Error' });
            }

            // 로그인 성공 응답
            const UserDatas = {
              user_id: rows[0].user_id,
              user_name: rows[0].user_name,
              birth_year: rows[0].birth_year,
              gender: rows[0].gender,
              user_gu: rows[0].user_gu,
              user_dong: rows[0].user_dong,
              user_introduce: rows[0].user_introduce,
              user_img: rows[0].user_img
            };

            res.json({
              rows: 'success',
              userId: user_id,
              accessToken: tokens.accessToken,
              refreshToken: tokens.refreshToken,
              user: UserDatas
            });
          });
        } else {
          res.json({ rows: 'failed' });
          console.error('비밀번호 불일치');
        }
      });
    } else {
      res.json({ rows: 'failed' });
      console.error('사용자가 존재하지 않음');
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

  bcrypt.hash(user_pw, 10, (err, hashedPw) => {
    if (err) {
      console.error('비밀번호 해시화 오류:', err);
      return res.status(500).json({ error: 'Password Hashing Error' });
    }

    const signUpSql = `insert into tb_user (user_id, user_pw, user_name, gender, birth_year, user_gu, user_dong, user_introduce)
    values(?, ?, ?, ?, ?, ?, ?, ?)`

    conn.query(signUpSql, [user_id, hashedPw, user_name, formattedGender, birth_year, user_gu, user_dong, user_introduce], (err, rows) => {
      console.log('회원가입 :', rows);
      if (err) {
        console.error('회원가입 실패:', err);
        res.json({ rows: 'failed' });
      } else {
        console.log('회원가입 성공');
        // 회원가입이 성공한 경우에만 실행됩니다.
        const badge_code = 'badge_code01'; // badge_code를 'badge_code01'로 설정

        // tb_user_badge에 badge_code와 user_id를 저장하는 쿼리
        const userBadgeSql = 'INSERT INTO tb_user_badge (badge_code, user_id) VALUES (?, ?)';
        
        conn.query(userBadgeSql, [badge_code, user_id], (err, userBadgeRows) => {
          if (err) {
            console.error('뱃지 저장 오류:', err);
            return res.status(500).json({ error: 'Badge Saving Error' });
          }

          console.log('뱃지 저장 성공:', userBadgeRows);
          res.json({ rows: 'success', user_id: user_id });
        });
      }
    });
  })
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

// 회원 탈퇴
router.post('/userDropOut', (req, res) => {
  console.log('일정 삭제 라우터', req.body);
  const { user_id } = req.body;

  const dropOutSql = `delete from tb_user where user_id = ?`

  conn.query(dropOutSql, [user_id], (err, rows) => {
    if (err) {
      console.error('회원 탈퇴 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('회원 탈퇴 성공');
      res.json({ rows: 'success' });
    }
  })
})

module.exports = router;