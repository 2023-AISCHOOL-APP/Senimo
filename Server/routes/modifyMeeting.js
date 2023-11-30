const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config');
const fs = require('fs');
const multer = require('multer');
const path = require('path');
const { v4: uuidv4 } = require('uuid');

const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        // 파일이 저장될 경로
        cb(null, 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads');
    },
    filename: function (req, file, cb) {
        // 파일 저장시 사용할 이름
        const uniqueFilename = uuidv4() + '-' + file.originalname;
        cb(null, uniqueFilename);
        console.log("사진이름: ", uniqueFilename);
    }
  });
  
  const upload = multer({ storage: storage });


router.post('/modifyMeeting', upload.single('picture') ,(req, res) => {
    //console.log('result', req.body);
    const meetingData = JSON.parse(req.body.modifyMeeting);
    const { user_id, club_name, club_introduce, max_cnt, attend_user_cnt, club_location, keyword_name, club_img, club_code} = meetingData;
    
    let oldImagePath = null;// 변수를 외부에 정의

    const getOldImagePathQuery = `select club_img from tb_club where club_code = ?`;
    conn.query(getOldImagePathQuery, [club_code], (err, results) => {
        if (err) {
            return res.status(500).send('Database error: ' +err.message);
        }
        oldImagePath = results[0]?.club_img;

        // 기존 이미지가 있고, 새 이미지가 업로드된 경우 기존 이미지 삭제
        if (oldImagePath && req.file) {
            fs.unlink(path.join("C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads/",oldImagePath), (err) => {
                if (err) console.error('Failed to delete old image:', err);
            });
        }
    })
    
    // 먼저 keyword_code를 조회합니다.
    const findKeywordCodeQuery = `SELECT keyword_code FROM tb_keyword WHERE keyword_name = ?;`;
    conn.query(findKeywordCodeQuery, [keyword_name], (err, results) => {
        if (err) {
            // 파일 삭제 로직
            if (req.file) {
                const filePath = 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads/' + req.file.filename;
                fs.unlink(filePath, err => {
                    if (err) console.error('Failed to delete uploaded file:', err);
                });
            }
            return res.status(500).json({ error: err.message });
        }
        if (results.length > 0) {
            // keyword_code를 찾았으면, 이제 모임을 생성합니다.
            const keyword_code = results[0].keyword_code;
             // 모임 정보 업데이트
            const newImagePath = req.file ? req.file.filename : oldImagePath;
            console.log("newImagePath",newImagePath);
            const updateQuery = `
            UPDATE tb_club
            SET keyword_code = ?, club_name = ?, club_location = ?, max_cnt = ?, club_introduce = ?, club_img = ?
            WHERE club_code = ?;            
            `;
            conn.query(updateQuery, [keyword_code,  club_name,  club_location, max_cnt, club_introduce, newImagePath, club_code], (err, result) => {
                if (err) {
                     // 파일 삭제 로직
                if (req.file) {
                const filePath = 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads/' + req.file.filename;
                fs.unlink(filePath, err => {
                    if (err) console.error('Failed to delete uploaded file:', err);
                });
            }
            return res.status(500).json({ error: err.message });
                    console.log("실패")

                } else {
                    res.status(200).json({ 
                    club_img: `${config.baseURL}/uploads/${newImagePath}`})
                }
            });
        }else {
            return res.status(404).json({ error: "모임 정보 수정에 실패하였습니다." });
        }
    });
});


module.exports = router;