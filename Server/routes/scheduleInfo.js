const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.get('/scheduleInfo/:sche_code', (req, res) => {
  console.log('result', req.body);
  let scheCode = req.params.club_code

  const query = `
  SELECT c.club_name, a.sche_img, a.sche_title, a.sche_date, a.sche_location, a.fee, COUNT(b.user_id) AS attend_user_cnt, a.max_num
  FROM tb_schedule a
  LEFT JOIN tb_sche_joined_user b ON a.sche_code = b.sche_code
  left join tb_club c on a.club_code = c.club_code
  WHERE a.sche_code = ?
  GROUP BY a.sche_img, a.sche_title, a.sche_date, a.sche_location, a.fee, a.max_num;
  `

  conn.query(query, [scheCode], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    if (results.length > 0) {
        console.log("안드로이드로 보내는 값", results);
        res.status(200).json({ data: results });
    } else {
        res.status(404).json({ error: "ScheduleInfo not found." });
    }
  });
});

module.exports = router;