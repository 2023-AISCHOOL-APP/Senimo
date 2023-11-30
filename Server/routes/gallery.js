const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const multer = require('multer');
const sharp = require('sharp');
const path = require('path');
const { v4: uuidv4 } = require('uuid');
const config = require('../config/config')
//const conn = require('../config/database');
const { UPLOADS_PATH } = require('../config/config');
require("dotenv").config();

// 데이터베이스 연결 설정 함수
async function connectDatabase() {
    return await mysql.createConnection({
        host: process.env.HOST,
        user: process.env.USER,
        password: process.env.PASSWORD,
        port: 3307,
        database: process.env.DATABASE
    });
}

// multer 설정
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads');
    },
    filename: function (req, file, cb) {
        const uniqueFilename = uuidv4() + '-' + file.originalname;
        cb(null, uniqueFilename);
    }
});
const upload = multer({ storage: storage });

router.post('/uploadPhotos', upload.array('picture'), async (req, res) => {
    console.log("사진이 업로드 요청이 왔습니다.")
    let conn;
    try {
        conn = await connectDatabase(); // 데이터베이스 연결
        await conn.beginTransaction();

        const galleryInfo = JSON.parse(req.body.galleryInfo);
        console.log("사진이 업로드되었습니다. :",galleryInfo)
        const files = req.files;
        console.log("사진이 업로드되었습니다. :",files)
        const { user_id, club_code } = galleryInfo;

        // tb_photo에 데이터 삽입
        await conn.query(`Insert into tb_photo (user_id, club_code) values (?, ?)`, [user_id, club_code]);
        console.log("photo_code가 생성되었습니다. :")
        // 생성된 photo_code 조회
        const [codeResults] = await conn.query(`select photo_code from tb_photo where user_id = ? order by photo_dt desc limit 1;`, [user_id]);
        const photo_code = codeResults[0].photo_code;
        console.log("photo_code가 생성되었습니다. :",photo_code)
        // 모든 사진 파일에 대한 처리
        for (const file of files) {
            // 썸네일 생성 및 tb_photo_img에 데이터 삽입
            const thumbnailPath = `thumb_${file.filename}`;
            await sharp(file.path).resize(200, 200).toFile(`${UPLOADS_PATH}/thumb/${thumbnailPath}`);
            await conn.query(`insert into tb_photo_img (img_name, img_original_name, img_thumb_name, img_size, img_ext, photo_code) values (?, ?, ?, ?, ?, ?)`, [file.filename, file.originalname, thumbnailPath, file.size, path.extname(file.originalname), photo_code]);
        }

        await conn.commit();
        res.json({ message: '사진이 성공적으로 업로드되었습니다.' });
        console.log("사진이 성공적으로 업로드되었습니다.")
    } catch (err) {
        if (conn) {
            await conn.rollback();
            console.log("사진이 업로드 롤백하였습니다.")
        }
        res.status(500).json({ error: err.message });
        console.log("사진이 업로드 실패하였습니다.")
    } finally {
        if (conn) {
            await conn.end();
        }
    }
});

// 사진첩 정보 조회 라우터
router.get('/getGallery', async (req, res) => {
    console.log("사진첩 정보조회 시작하겠습니다.");
    let conn;
    try {
        console.log("사진첩 정보조회 시작하겠습니다1.");
        conn = await connectDatabase(); // 데이터베이스 연결
        const club_code = req.query.club_code;
        const query = `
        SELECT 
            CONCAT('${config.baseURL}/uploads/thumb/', pimg.img_thumb_name) AS img_thumb_name,
            u.user_name,
            j.club_role,
            p.photo_dt
        FROM tb_photo_img pimg
        JOIN tb_photo p ON pimg.photo_code = p.photo_code
        JOIN tb_user u ON p.user_id = u.user_id
        JOIN tb_join j ON u.user_id = j.user_id
        WHERE p.club_code = ? AND j.club_code = p.club_code
        ORDER BY p.photo_dt DESC;
        `;
        const [results] = await conn.query(query, [club_code]);
        console.log("사진첩 정보조회 시작하겠습니다2.");
        if (results.length === 0) {
            res.status(200).json(results);
            console.log("사진첩 정보조회1 :", results);
        }else{
            res.json(results);
            console.log("사진첩 정보조회2 :", results);
        }
        
         // GalleryVO 정보를 클라이언트에 전송
        console.log("사진첩 정보조회 :", results);
    } catch (err) {
        res.status(500).send('서버 오류: ' + err.message);
        console.log("사진첩 정보조회 실패했습니다..");
    } finally {
        if (conn) {
            await conn.end();
        }
    }
});

module.exports = router;