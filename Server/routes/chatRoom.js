const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config');

router.get('/loadChatRooms', (req,res) => {
    console.log("채팅방 받으러 왔어요")
    const user_Id = req.query.userId;
    
    const selectClubQuery = `select club_code from tb_join where user_id = ?`;
    conn.query(selectClubQuery,[user_Id], (err,club_Code) => {
        if (err){
            return res.status(500).json({error : err.message});
        }
        console.log("club_Code1", club_Code)
        let chatRooms = [];
        let processed = 0;

        // 각 클럽의 최신 채팅 내용 조회
        club_Code.forEach(club => {
            // console.log("club입니다. ", club)
            const selectChat = `
            SELECT 
                concat('${config.baseURL}/uploads/', c.club_img) as club_img,      -- 모임 이미지
                c.club_name,     -- 모임 이름
                ch.chat_content, -- 마지막 발화 내용
                ch.chat_dt ,      -- 마지막 발화 시간
                u.user_name,        -- 유저이름
                c.club_code,		-- 클럼코드
                concat('${config.baseURL}/uploads/', u.user_img) as user_img			-- 유저이미지
            FROM 
                tb_club c
            JOIN 
                tb_join j ON c.club_code = j.club_code
            JOIN tb_user u ON u.user_id = j.user_id
            JOIN 
                (
                    SELECT 
                        chat_code, club_code, chat_content, chat_dt
                    FROM 
                        tb_chat
                    WHERE 
                        (club_code, chat_dt) IN (
                        SELECT 
                            club_code, MAX(chat_dt)
                        FROM 
                            tb_chat
                        GROUP BY 
                            club_code
                        )
                ) ch ON c.club_code = ch.club_code
WHERE 
    j.user_id = ?;`
            console.log("club입니다. ", club);
            const clubCode = club['club_code']; // 이렇게 club_code에 접근
            console.log("club_Code2", clubCode);
            console.log("user_Id", user_Id)
            console.log("processed 마지막0:",processed)
            conn.query(selectChat,[user_Id, clubCode], (err,chatRoom) => {
                if(err) {
                    return res.status(500).json({error : err.message});
                    console.log("chatRoom에 에러")
                }
                console.log("chatroom 마지막",chatRoom)
                console.log("chatroom 모음 마지막",chatRooms)
                console.log("processed 마지막1:",processed)
                if (chatRoom.length >0) {
                    chatRooms.push(chatRoom[0]);
                }

                processed++;
                console.log("processed 마지막:",processed)
                if(processed === club_Code.length) {
                    res.json(chatRoom); // 나중에 확인
                    console.log("채팅방 조회 :",chatRooms)
                }
            })
        })
    })
})

module.exports = router;