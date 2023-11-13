const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.get('/', (req, res) => {
  console.log('result', req.body);

  const query = `
    select c.club_name, c.club_introduce, c.max_cnt, k.keyword_name 
    from tb_club c 
    left join tb_club_keyword ck on c.club_code = ck.Club_code 
    left join tb_keyword k on ck.keyword_code = k.keyword_code
  `

  // const query = `select * from tb_user`

  // const query = `
  //   select c.* from tb_interest_club ic
  //   join tb_club c on ic.club_code = c.club_code
  //   where ic.user_kakao_token = 'user2'
  // `

  conn.query(query, (err, rows) => {
    console.log('rows :', rows);
    if (err) {
      res.status(500).send('서버 에러: ' + err.message);
    } else {
      // 결과를 JSON 형태로 클라이언트에 전송합니다.
      res.json(rows);
    }
  });
});

module.exports = router;