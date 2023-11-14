const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.get('/getMeetings', (req, res) => {
  console.log('result', req.body);
  // SQL 쿼리를 실행합니다.

  const query = `
  select c1.club_location, c1.club_name, c1.club_introduce, count(j.user_kakao_token) as attend_user_cnt, c1.max_cnt, c1.club_img, k.keyword_name
from tb_club c1 
left join tb_keyword k on c1.keyword_code = k.keyword_code
left join tb_join j on c1.club_code = j.club_code
group by c1.club_location, c1.club_name, c1.club_introduce, c1.max_cnt, c1.club_img, k.keyword_name
order by attend_user_cnt desc;`
  

//   const query = `SELECT c.club_name, c.club_introduce, c.max_cnt, c.club_location,
//   GROUP_CONCAT(DISTINCT k.keyword_name ORDER BY k.keyword_name) as keyword_name, 
//   COUNT(DISTINCT u.user_kakao_token) as 가입된_인원수
// FROM tb_club c
// LEFT JOIN tb_club_keyword ck ON c.club_code = ck.club_code
// LEFT JOIN tb_keyword k ON ck.keyword_code = k.keyword_code
// LEFT JOIN tb_join_club jc ON jc.club_code = c.club_code
// LEFT JOIN tb_user u ON jc.user_kakao_token = u.user_kakao_token
// GROUP BY c.club_name, c.club_introduce, c.max_cnt,c.club_location
// ORDER BY 가입된_인원수 DESC, c.club_name ASC;`

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


  router.get('/sche_intro/:sche_code', (req, res) => {
    // 클라이언트로부터 받은 schedule_id를 사용하여 쿼리문을 생성
    const sche_code = req.params.sche_code;
    console.log(req)
    console.log(sche_code)
    const query = `SELECT 
    s.sche_code,
    s.club_code,
    s.sche_title,
    s.sche_content,
    s.sche_date,
    s.sche_location,
    s.max_num,
    s.fee,
    s.sche_img,
    (SELECT COUNT(*) FROM tb_sche_joined_user WHERE sche_code = s.sche_code) AS joined_user_count
FROM 
    tb_schedule AS s
WHERE 
    s.sche_code = ?;
`;

     conn.query(query, [sche_code], (error, results) => {
      if (error) {
        return res.status(500).send('Server error occurred: ' + error.message);
      }
      res.json(results[0]); // 만약 결과가 하나만 예상된다면 첫 번째 결과만 반환
    });
  });


module.exports = router;