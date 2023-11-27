const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.get('/getSchedules/:club_code', (req, res) => {
  console.log('result', req.body);
  let clubCode = req.params.club_code

  const query = `
  SELECT s.sche_code, s.club_code, s.sche_title, s.sche_content, s.sche_date, s.sche_location, s.fee, s.max_num, COUNT(j.user_id) AS joined_members, s.sche_img, c.club_name
  FROM tb_schedule s LEFT JOIN tb_sche_joined_user j ON s.sche_code = j.sche_code JOIN tb_club c ON s.club_code = c.club_code
  WHERE s.club_code = ?
  GROUP BY s.sche_code
  `;

  conn.query(query, [clubCode], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    if (results.length > 0) {
        console.log("안드로이드로 보내는 값", results);
        res.status(200).json({ data: results });
    } else {
        res.status(404).json({ error: "Club Schedule not found." });
    }
  });
});

module.exports = router;