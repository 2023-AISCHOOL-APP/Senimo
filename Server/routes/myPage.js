const express = require('express')
const router = express.Router()
const conn = require('../config/database')
const config = require('../config/config');
const { json } = require('body-parser');
const e = require('express');


router.get('/getUserBadges/:userId', (req, res) => {
    const userId = req.params.userId;
    const sql = `SELECT tb_badge.badge_name
                 FROM tb_user_badge
                 JOIN tb_badge ON tb_user_badge.badge_code = tb_badge.badge_code
                 WHERE tb_user_badge.user_id = ?`;

    conn.query(sql, [userId], (error, results, fields) => {
        if (error) {
            res.status(500).send({ error: 'Something failed!' });
        } else {
            res.json(results);
        }
    });
});


module.exports = router;
