const express = require('express');
const router = express.Router();
const conn = require('../config/database');
const config = require('../config/config');

// 유저 정보 가져오기
router.get('/getUserProfile', (req, res) => {
    console.log("요청왔다")
    const userId = req.query.userId;

    if (!userId) {
        return res.status(400).json({ message: 'userId is required' });
    }

    // 데이터베이스에서 사용자 정보를 검색하는 쿼리
    const query = `
    SELECT user_name, gender, birth_year, user_gu, user_dong, user_introduce, CONCAT('${config.baseURL}/uploads/', user_img) AS user_img
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

// 유저 정보 업데이트
router.post('/editMyProfile', (req, res) => {
    console.log('result', req.body);
    const { user_id, user_name, gender, birth_year, user_gu, user_dong, user_introduce, user_img } = req.body;

    // 프로필 정보를 업데이트합니다.
    const updateQuery = `
        UPDATE tb_user
        SET user_name = ?, gender = ?, birth_year = ?, user_gu = ?, user_dong = ?, user_introduce = ?, user_img = ?
        WHERE user_id = ?;
    `;

    conn.query(updateQuery, [user_name, gender, birth_year, user_gu, user_dong, user_introduce, user_img, user_id], (err, result) => {
        if (err) {
            res.status(500).json({ error: err.message });
            console.log("실패");
        } else {
            console.log("바뀐 이름 : ", user_name);

            // 업데이트된 사용자 정보를 다시 불러옵니다.
            const selectQuery = `
                SELECT user_name, gender, birth_year, user_gu, user_dong, user_introduce, CONCAT('${config.baseURL}/uploads/', user_img) AS user_img
                FROM tb_user 
                WHERE user_id = ?;
            `;

            conn.query(selectQuery, [user_id], (selectErr, selectResult) => {
                if (selectErr) {
                    res.status(500).json({ error: selectErr.message });
                    console.log("실패");
                } else {
                    // 업데이트된 사용자 정보를 클라이언트에게 반환합니다.
                    res.status(200).json({ result: selectResult[0]});
                    console.log("보내는 값edit : ", selectResult[0],
                    "user_img_edit: " , `${config.baseURL}/uploads/${selectResult[0].user_img}`);
                }
            });
        }
    });
});



module.exports = router;
