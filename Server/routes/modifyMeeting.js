const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.post('/postModifyMeeting', (req, res) => {
    console.log('result', req.body);
    const { user_id, club_name, club_introduce, max_cnt, attend_user_cnt, club_location, keyword_name, club_img, club_code} = req.body;
    // 먼저 keyword_code를 조회합니다.
    const findKeywordCodeQuery = `SELECT keyword_code FROM tb_keyword WHERE keyword_name = ?;`;
    conn.query(findKeywordCodeQuery, [keyword_name], (err, results) => {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        console.log("1",results)
        if (results.length > 0) {
            // keyword_code를 찾았으면, 이제 모임을 생성합니다.
            const keyword_code = results[0].keyword_code;
            const updateQuery = `
            UPDATE tb_club
            SET keyword_code = ?, club_name = ?, club_location = ?, max_cnt = ?, club_introduce = ?, club_img = ?
            WHERE club_code = ?;            
            `;
            conn.query(updateQuery, [keyword_code,  club_name,  club_location, max_cnt, club_introduce, club_img, club_code], (err, result) => {
                if (err) {
                    res.status(500).json({ error: err.message });
                    console.log("실패")
                } else {
                    console.log("새로생성된id : ", result.insertId); 
                    res.status(200).json({ result: true })
                    console.log("성공",result);
                    console.log("보내는값 : ",result);
                }
            });
        }else {
            return res.status(404).json({ error: "모임 정보 수정에 실패하였습니다." });
        }
    });
});


module.exports = router;