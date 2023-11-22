const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.post('/updateMember', (req, res) => {
    console.log('updateMemberRequest result', req.body);
    const { club_code, user_id, club_role } = req.body;
    const updateQuery = `UPDATE tb_join SET club_role = ? WHERE user_id = ? and club_code = ?;` // 회원 역할 변경
    const findLeaderQuery = `SELECT user_id FROM tb_club WHERE club_code=?;`
    const setLeaderQuery = `UPDATE tb_club SET user_id =? WHERE club_code=?` // 모임장 아이디 가져오기
  
    // 신임 모임장 위임하기
    if(club_role == 1){
      conn.query(updateQuery,[club_role, user_id, club_code],(err, results) =>{
        if (err) {
          console.error("Error occurred: ", err);
          res.status(500).json({ error: err.message });
        } else {
          if (results.affectedRows === 0) {
            res.status(200).json({ success: true, message: "역할을 변경할 멤버가 없습니다." });
          } else {
            // 기존 모임장 -> 일반회원으로 전환
            conn.query(findLeaderQuery,[club_code],(err, leaderResults)=>{
              if (err) {
                console.error("Error occurred: ", err);
                res.status(500).json({ error: err.message });
              } else {
                if (leaderResults.affectedRows === 0) {
                  res.status(200).json({ success: true, message: "역할을 변경할 멤버가 없습니다." });
                } else {
                  const leader_id = leaderResults[0].user_id;
                  conn.query(updateQuery,[3, leader_id, club_code],(err, updateResults) =>{
                    if (err) {
                      console.error("Error occurred: ", err);
                      res.status(500).json({ error: err.message });
                    } else {
                      if (updateResults.affectedRows === 0) {
                        res.status(200).json({ success: true, message: "삭제할 멤버가 없습니다." });
                      } else {
                        // 클럽 모임장 아이디 변경
                        conn.query(setLeaderQuery,[leader_id, club_code],(err, setLeaderResults) =>{
                          if (err) {
                            console.error("Error occurred: ", err);
                            res.status(500).json({ error: err.message });
                          } else {
                            if (setLeaderResults.affectedRows === 0) {
                              res.status(200).json({ success: true, message: "모임장으로 지정할 멤버가 없습니다." });
                            } else {
                              res.status(200).json({ success: true, message: "모임장 변경 성공." });
                            }
                          }
                        });
                      }
                    }
                  });
                }
              }
            });
          }
          console.log('응답성공: ', results);
        }
      });
    } else {
      conn.query(updateQuery,[club_role, user_id, club_code],(err, results) =>{
        if (err) {
          console.error("Error occurred: ", err);
          res.status(500).json({ error: err.message });
        } else {
          if (results.affectedRows === 0) {
            res.status(200).json({ success: true, message: "삭제할 멤버가 없습니다." });
          } else {
            res.status(200).json({ success: true, message: "멤버 삭제 성공." });
          }
          console.log('응답성공: ', results);
        }
      });
    }
  }); 
module.exports = router;
