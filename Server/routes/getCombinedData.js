const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')
router.get('/getCombinedData/:user_id', (req, res) => {
  console.log('result', req.body);
  let userId = req.params.user_id

  // 모임 일정 가져오기: 기존 getSchedules쿼리 -> ScheduleVO
  const myScheduleQuery = `
  SELECT s.sche_code, s.club_code, s.sche_title, s.sche_content, s.sche_date, s.sche_location, s.fee, 
    s.max_num, (SELECT COUNT(*) FROM tb_sche_joined_user WHERE sche_code = s.sche_code) AS joined_members, CONCAT('${config.baseURL}/uploads/', s.sche_img) AS sche_img,a.club_name
    FROM tb_schedule s 
    JOIN tb_club a ON s.club_code = a.club_code
    WHERE s.sche_code IN (SELECT sche_code FROM tb_sche_joined_user WHERE user_id = ?)
    GROUP BY s.sche_code, s.club_code, s.sche_title, s.sche_content, s.sche_date, s.sche_location, s.fee, s.max_num, s.sche_img, a.club_name    
  `;

  // 내가 가입한 모임 가져오기 : 기존 getMeetings 쿼리 -> MeetingVO
  const myClubQuery = `
  SELECT c.club_code, c.club_location, c.club_name, c.club_introduce, 
    (SELECT COUNT(*) FROM tb_join WHERE club_code = c.club_code) AS attend_user_cnt, c.max_cnt, 
    c.club_img,k.keyword_name
  FROM tb_club c 
  LEFT JOIN tb_keyword k ON c.keyword_code = k.keyword_code
  WHERE c.club_code IN (SELECT club_code FROM tb_join WHERE user_id = ?)
  GROUP BY c.club_code, c.club_location, c.club_name, c.club_introduce, c.max_cnt, c.club_img, k.keyword_name
  `;
  

  // 내 관심 모임 가져오기 : MeetingVO
  const myInterestQuery = `
    SELECT c.club_location, c.club_name, c.club_introduce, k.keyword_name,
      COUNT(j.user_id) AS attend_user_cnt, c.max_cnt, c.club_img, c.club_code, ic.user_id
    FROM tb_interested_club ic
    JOIN tb_club c ON ic.club_code = c.club_code
    JOIN tb_keyword k ON c.keyword_code = k.keyword_code
    LEFT JOIN tb_join j ON c.club_code = j.club_code
    WHERE ic.user_id = ?
    GROUP BY c.club_code, c.club_location, c.club_name, c.club_introduce, k.keyword_name, c.max_cnt, c.club_img;
  `

  conn.query(myScheduleQuery, [userId], (err, myScheduleResults) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    conn.query(myClubQuery, [userId], (err, myClubResults) => {
      if (err) {
        return res.status(500).json({ error: err.message });
      }

      conn.query(myInterestQuery, [userId], (err, myInterestResults) => {
        if (err) {
          return res.status(500).json({ error: err.message });
        }

        console.log("안드로이드로 보내는 값", {
          mySchedule: myScheduleResults,
          myClub: myClubResults,
          myInterestedClub: myInterestResults
        });
        res.status(200).json({
          mySchedule: myScheduleResults,
          myClub: myClubResults,
          myInterestedClub: myInterestResults
        });
      });
    });
  });
});

module.exports = router;