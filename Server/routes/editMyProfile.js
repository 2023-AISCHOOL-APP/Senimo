const express = require('express');
const router = express.Router();
const conn = require('../config/database');
const config = require('../config/config');

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
                SELECT user_name, gender, birth_year, user_gu, user_dong, user_introduce, user_img
                FROM tb_user
                WHERE user_id = ?;
            `;

            conn.query(selectQuery, [user_id], (selectErr, selectResult) => {
                if (selectErr) {
                    res.status(500).json({ error: selectErr.message });
                    console.log("실패");
                } else {
                    // 업데이트된 사용자 정보를 클라이언트에게 반환합니다.
                    res.status(200).json({ result: selectResult[0] });
                    console.log("보내는 값 : ", selectResult[0]);
                }
            });
        }
    });
});

module.exports = router;
