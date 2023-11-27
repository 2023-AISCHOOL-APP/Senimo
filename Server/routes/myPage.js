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
        b.badge_name
    FROM 
        tb_user_badge ub
    JOIN 
        tb_badge b ON ub.badge_code = b.badge_code
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

router.get('/getUserProfile', (req, res) => {
    console.log("요청왔다")
    const userId = req.query.userId;

    if (!userId) {
        return res.status(400).json({ message: 'userId is required' });
    }

    // 데이터베이스에서 사용자 정보를 검색하는 쿼리
    const query = `
    SELECT user_name, gender, birth_year, user_gu, user_dong, user_introduce, user_img
    FROM tb_user
    WHERE user_id = ?;
`;

conn.query(query, [userId], (err, results) => {
    if (err) {
        // 쿼리 실행 중 에러 발생 시
        return res.status(500).json({ error: err.message });
    }

    if (results.length === 0) {
        // 사용자를 찾을 수 없는 경우
        return res.status(404).json({ message: 'User not found' });
    }

    // 사용자 정보를 성공적으로 찾은 경우
    res.status(200).json(results[0]);
});


});


module.exports = router;
