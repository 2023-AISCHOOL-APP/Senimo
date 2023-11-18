const express = require('express')
const router = express.Router()
const conn = require('../config/database');


router.post('/postcreateMeeting', (req, res) => {
    console.log('result', req.body);
    const { user_id, club_name, club_introduce, max_cnt, club_location, keyword_name, club_img_url } = req.body;
    
    const insertQuery = `
    INSERT INTO tb_meeting (user_id ,club_name ,club_introduce ,max_cnt ,club_location, keyword_code ,club_img)
    VALUES (?, ?, ?, ?, ?, ?, ?);
`;

    conn.query(insertQuery, [user_id, club_name, club_introduce, max_cnt, club_location, keyword_name, club_img_url], (err, results, fields) => {
        if (err) {
            res.status(500).json({ error: err.message });
        } else {
            res.status(200).json({ message: '모임이 생성되었습니다' });
        }
    });
});


module.exports = router;