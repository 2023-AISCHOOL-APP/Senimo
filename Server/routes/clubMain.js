const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.get('/getClubInfo/:club_code', (req, res) => {
  //console.log('result', req.body);
  let clubCode = req.params.club_code

  const query = `
    select c1.club_code, c1.club_img, count(j.user_id) as joined_user_cnt, c1.max_cnt, c1.club_name, c1.club_location, k.keyword_name, c1.club_introduce,CONCAT('${config.baseURL}/uploads/', c1.club_img) AS club_img 
    from tb_club c1 
    left join tb_keyword k on c1.keyword_code = k.keyword_code
    left join tb_join j on c1.club_code = j.club_code
    where c1.club_code = ?
  `

  conn.query(query, [clubCode], (err, rows) => {
    //console.log('clubMainrows :', rows);
    if (err) {
      res.status(500).send('서버 에러: ' + err.message);
    } else {
      // 결과를 JSON 형태로 클라이언트에 전송합니다.
      res.json(rows[0]);
    }
  });
});

module.exports = router;