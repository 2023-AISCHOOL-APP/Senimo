const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.post('/clubMember', (req, res) => {
  console.log('result', req.body);
  const { club_code } = req.body; // Assuming club_code is passed in the request body

  const findMemberQuery = `
      SELECT b.user_img, b.user_name, a.club_role
      FROM tb_join a
      LEFT JOIN tb_user b ON a.user_id = b.user_id
      WHERE a.club_code = ?;
  `;

  conn.query(findMemberQuery, [club_code], (err, results) => {
      if (err) {
          return res.status(500).json({ error: err.message });
      }

      console.log(results);

      if (results.length > 0) {
          res.status(200).json({ members: results });
      } else {
          res.status(404).json({ error: "Club members not found." });
      }
  });
});

module.exports = router;