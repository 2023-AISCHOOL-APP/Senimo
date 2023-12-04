const express = require('express');
const router = express.Router();
const conn = require('../config/database');
const config = require('../config/config');
const fs = require('fs');
const multer = require('multer');
const path = require('path');
const sharp = require('sharp');
const { v4: uuidv4 } = require('uuid');
const { UPLOADS_PATH } = require('../config/config');

// multer 설정
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        // 파일이 저장될 경로
        cb(null, 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads');
    },
    filename: function (req, file, cb) {
        // 파일 저장시 사용할 이름
        const uniqueFilename = uuidv4() + '-' + file.originalname;
        cb(null, uniqueFilename);
        // console.log("사진이름: ", uniqueFilename);
    }
  });
  
  const upload = multer({ storage: storage });


// 유저 정보 가져오기
router.get('/getUserProfile', (req, res) => {
    const userId = req.query.userId;
    console.log("유저정보 가져오기..",req.query)

    if (!userId) {
        return res.status(400).json({ message: 'userId is required' });
    }

    // 데이터베이스에서 사용자 정보를 검색하는 쿼리
    const query = `
    SELECT user_id, user_name, gender, birth_year, user_gu, user_dong, user_introduce, CONCAT('${config.baseURL}/uploads/thumb/', user_img) AS user_img
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
    console.log("유저정보",results[0])
});


});

// 유저 정보 업데이트
router.post('/editMyProfile', upload.single('picture'),(req, res) => {
    console.log('result123', req.body);
    const file = req.file;
    const profile = JSON.parse(req.body.profile);
    const { user_id, user_name, gender, birth_year, user_gu, user_dong, user_introduce, user_img,imageChanged } = profile;
    let oldImagePath = null;// 변수를 외부에 정의

    const getOldImagePathQuery = `select user_img from tb_user where user_id = ?`;
    conn.query(getOldImagePathQuery, [user_id], (err,results) => {
      if (err) {
          return res.status(500).send('Database error: ' +err.message);
      }
      oldImagePath = results[0]?.user_img;
  
      let newImagePath;
      
      // 기존 이미지가 있고, 새 이미지가 업로드된 경우 기존 이미지 삭제
      if (imageChanged && req.file) {
        // 새 이미지 업로드 처리
        newImagePath = `thumb_${file.filename}`;
        sharp(file.path).resize(200, 200).toFile(`${UPLOADS_PATH}/thumb/${newImagePath}`);
        // 기존 이미지 삭제
          if(oldImagePath){
              fs.unlink(path.join(`${UPLOADS_PATH}/thumb/${oldImagePath}`), (err) => {
                if (err) console.error('Failed to delete old image:', err);
            });
          }
        } else {
          //기존 이미지 유지
          newImagePath = oldImagePath;
        }
    
    console.log('result1235', req.body.profile);
    console.log('result1234', user_id);
    // 프로필 정보를 업데이트합니다.
    const updateQuery = `
        UPDATE tb_user
        SET user_name = ?, gender = ?, birth_year = ?, user_gu = ?, user_dong = ?, user_introduce = ?, user_img = ?
        WHERE user_id = ?;
    `;

    conn.query(updateQuery, [user_name, gender, birth_year, user_gu, user_dong, user_introduce, newImagePath, user_id], (err, result) => {
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
                    console.log("보내는 값edit : ", selectResult[0],)
                    //"user_img_edit: " , `${config.baseURL}/uploads/${selectResult[0].user_img}`);
                }
             });
         }
        });
    });
});



module.exports = router;
