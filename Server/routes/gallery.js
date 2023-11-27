const express = require('express');
const router = express.Router();
const conn = require('../config/database');
const config = require('../config/config');
const multer = require('multer');
const path = require('path');
const sharp = require('sharp');
const fs = require('fs');
const { v4: uuidv4 } = require('uuid');

// multer 설정
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads'); // 저장 경로
    },
    filename: function (req, file, cb) {
        // 파일 저장시 사용할 이름
        const uniqueFilename = uuidv4() + '-' + file.originalname;
        cb(null, uniqueFilename);
        console.log("사진이름: ", uniqueFilename);
    }
});
const upload = multer({ storage: storage });

router.post('/uploadPhotos', upload.array('picture'),  (req, res) => {
    // 트랜잭션 시작
    conn.beginTransaction(err => {
        if (err) {
            return res.status(500).json({error: '트랜잭션 시작 오류 :' +err.message})
            console.log("gallery트랜잭션 시작 오류",err.message);
        }
        
        // JSON 문자열을 객체로 변환
        const galleryInfo = JSON.parse(req.body.galleryInfo);
        const files = req.files;
        const { user_id, club_code, club_role, img_name, user_img, user_name,img_original_name} = galleryInfo;
        for (const file of files) {
            
        // 썸네일 생성
        const thumbnailPath = 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads/thumb/thumb' + uniqueFilename;
            sharp(file.path)
            .resize(200, 200) // 썸네일 크기 조정
            .toFile(thumbnailPath);
        }
        let fileDetails = files.map(file => {
        return {
            fileSize: file.size,
            fileName: file.filename,
            fileExt: path.extname(file.filename)
        };
        });

        console.log("fileDetails :", fileDetails);
        console.log("galleryInfo :", galleryInfo);
    
        // 1단계 : tb_photo 테이블에 데이터 삽입
        const photoInsertQuery = `Insert into tb_photo (user_id, club_code) values (? ,?)`;
        conn.query(photoInsertQuery,[user_id,club_code], (err,insertResult)=>{
            if (err) {
                return conn.rollback(() => {
                    res.status(500).json({ error: '모임 생성 오류: ' + err.message });
                    console.log("1단계: 모임 생성",err.message);
                });
            }

            // 2단계 : 생성된 photo_code 조회
            const photoCodeQuery = `select photo_code from tb_photo where user_id = ? order by photo_dt desc limit 1;`;
            conn.query(photoCodeQuery,[user_id],(err,codeResults) => {
                if (err || codeResults.length === 0) {
                    return conn.rollback(() => {
                        res.status(500).json({ error: '포토 코드 조회 오류: ' + err.message });
                        console.log("2단계: 생성된 포토 코드 조회",err.message);
                    });
                }

                const photo_code = codeResults[0].photo_code;
                console.log("2단계 생성된 클럽코드", photo_code);

                // 3단계 : photo_code로 사진첩 이미지 등록
                const photoImgInsertQuery = `insert into tb_photo_img (img_name, img_original_name, img_thumb_name ,img_size ,img_ext ,photo_code)
                values (?, ?, ?, ?, ?, ?)`;
                conn.query(photoImgInsertQuery,[file.originalname,uniqueFilename,thumb+uniqueFilename,fileSize,fileExt,photo_code], (err,imgResult) =>{
                    if (err) {
                        console.error("사진첩 이미지 등록 실패: ", err);
                        return conn.rollback(() => {
                            res.status(500).json({ error: '사진첩 이미지 등록 오류: ' + err.message });
                            console.log(" 3단계: 사진첩 이미지 등록",err.message);
                        });
                    }

                    // 4딘계 : 사진업로드한 사람의 role 조회
                    const clubRoleQuery = `SELECT j.club_role
                    FROM tb_join j
                    WHERE j.user_id = ? AND j.club_code = ?;`
                    conn.query(clubRoleQuery, [user_id,club_code], (err,roleResult) =>{
                        if (err || codeResults.length === 0) {
                            return conn.rollback(() => {
                                res.status(500).json({ error: 'role 조회 오류: ' + err.message });
                                console.log("2단계: 사진업로드한 사람 role 조회",err.message);
                            });
                        }
                    
                    const club_role = roleResult[0].club_role;
                    console.log("4단계 club_role 조회 : ", photo_code);
                    // 트랜잭션 커밋
                    conn.commit(err => {
                        if (err) {
                            console.error("커밋 실패: ", err);
                            return conn.rollback(() => {
                                res.status(500).json({ error: '트랜잭션 커밋 오류: ' + err.message });
                                console.log(" 트랜잭션 커밋",err.message);
                            });
                        }

                        // 성공응답
                        res.json({
                            message: '모임이 성공적으로 생성되었습니다.',
                            club_code,
                            club_name,
                            user_name,
                            img_thumb_name: `${config.baseURL}/uploads/${thumbnailPath}`,
                            club_role
                        }); 
                        console.log("성공적으로 사진을 업로드하였습니다.")

                        }); 
                    });
                }); //photoImgInsertQuery
            }); //photoCodeQuery
        
        });// photoInsertQuery
    }); // 트랜잭션 끝
});




module.exports = router;