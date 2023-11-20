const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')


  // 'public' 폴더를 정적 파일로 제공
 

router.get('/getMeetings', (req, res) => {
  console.log('result', req.body);

  // SQL 쿼리를 실행합니다.

  const query = `
  SELECT 
    c.club_code,
    c.club_location, 
    c.club_name, 
    c.club_introduce, 
    COUNT(j.user_id) AS attend_user_cnt, 
    c.max_cnt, 
    CONCAT('${config.baseURL}/uploads/', c.club_img) AS club_img ,
    k.keyword_name,
    c.user_id
FROM 
    tb_club c
LEFT JOIN 
    tb_keyword k ON c.keyword_code = k.keyword_code
LEFT JOIN 
    tb_join j ON c.club_code = j.club_code
GROUP BY 
    c.club_code,
    c.club_location, 
    c.club_name, 
    c.club_introduce, 
    c.max_cnt, 
    c.club_img, 
    k.keyword_name
ORDER BY 
    attend_user_cnt DESC
LIMIT 20;`
  

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