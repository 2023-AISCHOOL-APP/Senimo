const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.get('/getMeetings', (req, res) => {
  console.log('result', req.body);

  // SQL 쿼리를 실행합니다.

  const query = `
  select c1.club_location, c1.club_name, c1.club_introduce, count(j.user_id) as attend_user_cnt, c1.max_cnt, c1.club_img, k.keyword_name
from tb_club c1 
left join tb_keyword k on c1.keyword_code = k.keyword_code
left join tb_join j on c1.club_code = j.club_code
group by c1.club_location, c1.club_name, c1.club_introduce, c1.max_cnt, c1.club_img, k.keyword_name
order by attend_user_cnt desc;`
  

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