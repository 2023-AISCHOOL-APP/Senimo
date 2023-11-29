const express = require('express')
const router = express.Router()
const conn = require('../config/database')

router.get('/getUserBadge/:user_id', (req, res) => {
    console.log('뱃지 라우터', req.params);
    let user_id = req.params.user_id

    const badgeSql = `select badge_get_code, badge_code, user_id from tb_user_badge where user_id = ?;`

    conn.query(badgeSql, [user_id], (err, results) => {
        console.log('뱃지 results :', results);
        if (err) {
            res.status(500).json({ error: err.message });
        }

        if (results.length > 0) {
            console.log("안드로이드로 보내는 badge data", results);
            res.status(200).json({ badges: results });
        } else {
            res.status(404).json({ error: "badge data not found." });
        }
    });
});

module.exports = router;
