const express = require('express')
const router = express.Router()
const conn = require('../config/database')

router.get('/getUserBadge/:user_id', (req, res) => {
	//console.log('뱃지 라우터', req.params);
	let user_id = req.params.user_id;

	const badgeSql = `SELECT badge_get_code, badge_code, user_id FROM tb_user_badge WHERE user_id = ?;`;
	const badgeCntSql = `SELECT COUNT(*) AS badge_cnt FROM tb_user_badge WHERE user_id = ?;`;

	conn.query(badgeSql, [user_id], (err, badgeResults) => {
		//console.log('뱃지 results :', badgeResults);
		if (err) {
			res.status(500).json({ error: err.message });
			return;
		}

		conn.query(badgeCntSql, [user_id], (countErr, countResults) => {
			if (countErr) {
				res.status(500).json({ error: countErr.message });
				return;
			}

			const totalBadges = countResults[0].badge_cnt;

			if (badgeResults.length > 0) {
				//console.log("뱃지 정보:", badgeResults);
				//console.log("뱃지 갯수:", totalBadges);

				if (totalBadges === 8) {
					const badge_code = 'badge_code09';
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
										res.json({ badges: badgeResults, badgeCnt: totalBadges });
										console.log("뱃지 저장 성공");
									}
								});
							} else {
								res.json({ badges: badgeResults, badgeCnt: totalBadges});
								console.log("해당 뱃지 코드와 사용자 ID의 조합이 이미 존재합니다.");
							}
						}
					});
				} else {
					res.status(200).json({ badges: badgeResults, badgeCnt: totalBadges });
				}
			} else {
				res.status(404).json({ error: "뱃지 데이터를 찾을 수 없습니다." });
			}
		});
	});
});

module.exports = router;
