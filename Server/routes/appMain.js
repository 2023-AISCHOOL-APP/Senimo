const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.get('/', (req, res) => {
  console.log('result', req.body);
  // SQL 쿼리를 실행합니다.
  // const query = `
  // select A.club_name, A.club_introduce, A.max_cnt, A.club_gu, k.keyword_name
  // from tb_club A
  // left join tb_club_keyword B on A.club_code = B.club_code
  // left join tb_keyword C on B.keyword_code = C.keyword_code`;

  const query = `select * from tb_user`

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