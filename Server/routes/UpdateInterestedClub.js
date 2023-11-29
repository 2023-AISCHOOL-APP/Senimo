const express = require('express')
const router = express.Router()
const conn = require('../config/database');

router.post('/updateInterestedClub', (req, res) => {
	console.log('result', req.body);
	const { club_code, user_id } = req.body;
	const InterestQuery = `SELECT interest_code FROM tb_interested_club WHERE user_id = ? AND club_code = ?;`;

	conn.query(InterestQuery, [user_id, club_code], (err, result) => {
		if (err) {
			res.status(500).json({ error: err.message });
			console.log("실패");
		} else {
			if (result.length > 0) {
				// 관심 모임이 이미 있는 경우
				const deleteInterestQuery = `DELETE FROM tb_interested_club WHERE user_id = ? AND club_code = ?;`;
				conn.query(deleteInterestQuery, [user_id, club_code], (err, result) => {
					if (err) {
						res.status(500).json({ error: err.message });
						console.log("데이터 삭제 실패");
					} else {
						res.status(200).json({ result: false });
						console.log("관심 모임 삭제됨");
					}
				});
			} else {
				// 관심 모임이 없는 경우
				const insertInterestQuery = `INSERT INTO tb_interested_club (club_code, user_id) VALUES (?, ?);`;
				conn.query(insertInterestQuery, [club_code, user_id], (err, result) => {
					if (err) {
						res.status(500).json({ error: err.message });
						console.log("데이터 추가 실패");
					} else {
						// 관심 모임 추가 후, 관심 모임 개수 확인하여 뱃지 추가
						const countInterestsQuery = `SELECT COUNT(*) AS interestCount FROM tb_interested_club WHERE user_id = ?;`;
						conn.query(countInterestsQuery, [user_id], (err, countResult) => {
							if (err) {
								res.status(500).json({ error: err.message });
								console.log("관심 모임 수 조회 실패");
							} else {
								const interestCount = countResult[0].interestCount;

								if (interestCount === 5) {
									const badge_code = 'badge_code05';
									const checkUserBadgeQuery = `SELECT * FROM tb_user_badge WHERE badge_code = ? AND user_id = ?;`;

									conn.query(checkUserBadgeQuery, [badge_code, user_id], (err, badgeResult) => {
										if (err) {
											res.status(500).json({ error: err.message });
											console.log("뱃지 조회 오류");
										} else {
											if (badgeResult.length === 0) {
												const insertUserBadgeQuery = `INSERT INTO tb_user_badge (badge_code, user_id) VALUES (?, ?);`;

												conn.query(insertUserBadgeQuery, [badge_code, user_id], (err, userBadgeResult) => {
													if (err) {
														res.status(500).json({ error: err.message });
														console.log("뱃지 저장 실패");
													} else {
														res.json({ result: true });
														console.log("뱃지 저장 성공");
													}
												});
											} else {
												res.json({ result: true });
												console.log("해당 뱃지 코드와 사용자 ID의 조합이 이미 존재합니다.");
											}
										}
									});
								} else {
									res.json({ result: true });
									console.log("관심 모임 추가됨");
								}
							}
						});
					}
				});
			}
		}
	});
});


module.exports = router;