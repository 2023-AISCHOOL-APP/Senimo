const express = require('express')
const router = express.Router()
const conn = require('../config/database');


router.get('/sche_intro/:sche_code', (req, res) => {
    console.log('result', req.body);
    // 클라이언트로부터 받은 schedule_id를 사용하여 쿼리문을 생성
    const sche_code = req.params.sche_code;
    console.log("요청",req)
    console.log("요청받은코드",sche_code)
    const query = `SELECT c.club_name, CONCAT('http://192.168.70.20:3333/uploads/', a.sche_img) AS sche_img_url , a.sche_title, a.sche_date, a.sche_location, a.fee, 
    COUNT(b.user_id) AS attend_user_cnt, a.max_num, a.sche_content
  FROM tb_schedule a
  LEFT JOIN tb_sche_joined_user b ON a.sche_code = b.sche_code
  left join tb_club c on a.club_code = c.club_code
  WHERE a.sche_code = ?
  GROUP BY a.sche_img, a.sche_title, a.sche_date, a.sche_location, a.fee, a.max_num;
  ;
`;

     conn.query(query, [sche_code], (error, results) => {
      if (error) {
        return res.status(500).send('Server error occurred: ' + error.message);
      }
      res.json(results[0]); // 만약 결과가 하나만 예상된다면 첫 번째 결과만 반환
      console.log("보낸결과",results)
    });
  });


module.exports = router;