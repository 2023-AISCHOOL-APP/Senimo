const express = require('express')
const router = express.Router()
const conn = require('../config/database')

router.get('/getUserBadge/:user_id', (req, res) => {
    console.log('뱃지 라우터', req.params);
    let user_id = req.params.user_id

    const badgeSql = `select badge_get_code, badge_code, user_id from tb_user_badge where user_id = ?;`
    const badgeCntSql = `select count(*) as badge_cnt from tb_user_badge where user_id = ?;`;

    conn.query(badgeSql, [user_id], (err, badgeResults) => {
        console.log('뱃지 results :', badgeResults);
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }

        conn.query(badgeCntSql, [user_id], (countErr, countResults) => {
            if (countErr) {
                res.status(500).json({ error: countErr.message });
                return;
            }

            const totalBadges = countResults[0].badge_cnt;

            if (badgeResults.length > 0) {
                console.log("뱃지 정보:", badgeResults);
                console.log("뱃지 갯수:", totalBadges);

                res.status(200).json({ badges: badgeResults, badgeCnt: totalBadges });
            } else {
                res.status(404).json({ error: "뱃지 데이터를 찾을 수 없습니다." });
            }
        });
    });
});

module.exports = router;
