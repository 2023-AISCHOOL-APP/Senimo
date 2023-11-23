const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

// 모임 생성
router.post('/postcreateMeeting', (req, res) => {
	console.log('result', req.body);
	const { user_id, club_name, club_introduce, max_cnt, club_location, keyword_name, club_img, } = req.body;
	// 먼저 keyword_code를 조회합니다.
	const findKeywordCodeQuery = `SELECT keyword_code FROM tb_keyword WHERE keyword_name = ?;`;
	conn.query(findKeywordCodeQuery, [keyword_name], (err, results) => {
		if (err) {
			return res.status(500).json({ error: err.message });
		}
		console.log("1", results)
		if (results.length > 0) {
			// keyword_code를 찾았으면, 이제 모임을 생성합니다.
			const keyword_code = results[0].keyword_code;
			const insertQuery = `
            INSERT INTO tb_club (user_id ,club_name ,club_introduce ,max_cnt ,club_location, keyword_code ,club_img)
            VALUES (?, ?, ?, ?, ?, ?, ?);`;
			conn.query(insertQuery, [user_id, club_name, club_introduce, max_cnt, club_location, keyword_code, club_img], (err, result) => {
				if (err) {
					res.status(500).json({ error: err.message });
					console.log("실패")
				} else {
					console.log("새로생성된id : ", result.insertId);
					res.status(200).json({
						message: '모임이 생성되었습니다',
						club_name,
						club_introduce,
						max_cnt,
						club_location,
						keyword_name,
						club_img: `${config.baseURL}/uploads/${club_img}`
					});
					console.log("성공");
					console.log("보내는값 : ", club_name, club_introduce, max_cnt, club_location, keyword_name, club_img);
				}
			});
		} else {
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