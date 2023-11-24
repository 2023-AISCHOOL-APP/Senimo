const express = require('express')
const router = express.Router()
const conn = require('../config/database')
const config = require('../config/config');




router.get('/getUserBadges', (req, res) => {
    console.log("요청왔다")
    const userId = req.query.userId;

    // SQL 쿼리를 실행
    const query = `
    SELECT
        b.badge_code, 
        b.badge_name, 
        ub.badge_get_dt, 
        u.user_id, 
        u.user_name, 
        u.gender, 
        u.birth_year, 
        u.user_gu, 
        u.user_dong, 
        u.user_introduce, 
        u.user_img
    FROM 
        tb_user_badge ub
    JOIN 
        tb_badge b ON ub.badge_code = b.badge_code
    JOIN 
        tb_user u ON ub.user_id = u.user_id
    WHERE 
        ub.user_id = ?`;

        conn.query(query, [userId], (err, rows) => {
        console.log('rows :', rows);
        if (err) {
            res.status(500).send('서버 에러: ' + err.message);
        } else {
            // 결과를 JSON 형태로 클라이언트에 전송합니다.
            res.json({result:rows});
        }
    });
});


module.exports = router;
