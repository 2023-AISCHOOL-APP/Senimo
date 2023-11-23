const express = require('express');
const router = express.Router();
const conn = require('../config/database');
const config = require('../config/config');
const multer = require('multer');
const { v4: uuidv4 } = require('uuid');


const club_code = uuidv4(); // UUID 생성
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
        console.log("사진이름: ", uniqueFilename);
    }
});

const upload = multer({ storage: storage });

// 모임 정보 생성 및 사진 업로드 라우트
router.post('/postCreateMeeting',upload.single('picture'), (req, res) => {
    console.log('Request Body:', req.body);

    // JSON 문자열을 객체로 변환
    const meetingData = JSON.parse(req.body.meeting);

    const { user_id, club_name, club_introduce, max_cnt, club_location, keyword_name} = meetingData;
    const club_img = req.file ? req.file.filename : null; // 업로드된 파일 이름const club_img = req.file ? req.file.filename : null; // 업로드된 파일 이름
    console.log('club_img 사진: ', club_img);
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
            const insertQuery = `
            INSERT INTO tb_club (user_id ,club_name ,club_introduce ,max_cnt ,club_location, keyword_code ,club_img)
            VALUES (?, ?, ?, ?, ?, ?, ?);`;
            conn.query(insertQuery, [user_id, club_name, club_introduce, max_cnt, club_location, keyword_code, club_img], (err, result) => {
                if (err) {
                    res.status(500).json({ error: err.message });
                    console.log("실패",err.message)
                } else {
                    console.log("새로생성된id : ", result.insertId); 
                    const club_img_url = `${config.baseURL}/uploads/${club_img}`
                    res.status(200).json({ message: '모임이 생성되었습니다' ,
                    club_name, 
                    club_introduce, 
                    max_cnt, 
                    club_location, 
                    keyword_name, 
                    club_img : club_img_url});
                    console.log("clubcode: ");
                    console.log("성공");
                    console.log("보내는값 : ",club_name,club_introduce,max_cnt,club_location,keyword_name,club_img,club_img_url);
                }
            });
        }else {
            return res.status(404).json({ error: "해당하는 키워드가 없습니다." });
        }
    });
});


// 일반회원 모임 가입
router.post('/joinClub', (req, res) => {
	console.log('joinClub router', req.body);
	const { club_code, user_id } = req.body

	const joinClubSql = `insert into tb_join (club_code, user_id) values (?,?)`

	conn.query(joinClubSql, [club_code, user_id], (err, rows) => {
		console.log('모임 가입 : ', rows);
		if (err) {
			console.error('모임 가입 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('모임 가입 성공');
      res.json({ rows: 'success'});
    }
	})
})

// 모임 탈퇴
router.post('/quitClub', (req, res) => {
	console.log('quitClub router', req.body);
	const { club_code, user_id } = req.body

	const joinClubSql = `delete from tb_join where club_code = ? and user_id = ?`

	conn.query(joinClubSql, [club_code, user_id], (err, rows) => {
		console.log('모임 탈퇴 : ', rows);
		if (err) {
			console.error('모임 탈퇴 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('모임 탈퇴 성공');
      res.json({ rows: 'success'});
    }
	})
})

module.exports = router;