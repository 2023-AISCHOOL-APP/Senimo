const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.post('/initialInterestedClub', (req, res) => {
  // console.log('result', req.body);
  const { club_code, user_id } = req.body;
  const InterestQuery = `SELECT interest_code FROM tb_interested_club WHERE user_id = ? AND club_code = ?;`;
  conn.query(InterestQuery, [user_id, club_code], (err, result) => {
    if (err) {
        res.status(500).json({ error: err.message });
        console.log("실패");
    } else {
        // Check if the result array has any entries
        const hasInterest = result.length > 0;
        res.status(200).json({ result: hasInterest });
        console.log(hasInterest ? "관심 모임 있음" : "관심 모임 없음");
    }
  })
});

module.exports = router;