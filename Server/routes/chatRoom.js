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
            console.log("club입니다. ", club)
            const selectChat = `
            SELECT 
                concat('${config.baseURL}/uploads/', u.user_img) as user_img, u.user_name, ch.chat_content, ch.chat_dt, 
                j.club_role, c.club_name
            FROM 
                tb_chat ch
            JOIN 
                tb_user u ON ch.user_id = u.user_id
            JOIN 
                tb_club c on c.user_id = u.user_id    
            JOIN 
                tb_join j ON ch.club_code = j.club_code AND ch.user_id = j.user_id
            JOIN
                (SELECT club_code, join_dt FROM tb_join WHERE user_id = ?) as user_join ON ch.club_code = user_join.club_code
            WHERE 
                ch.club_code = ?
                AND ch.chat_dt >= user_join.join_dt
            ORDER BY 
            ch.chat_dt;`
            console.log("club입니다. ", club);
            const clubCode = club['club_code']; // 이렇게 club_code에 접근
            console.log("club_Code2", clubCode);
            console.log("user_Id", user_Id)
            conn.query(selectChat,[user_Id, clubCode], (err,chatRoom) => {
                if(err) {
                    return res.status(500).json({error : err.message});
                    console.log("chatRoom에 에러")
                }
                console.log("chatroom",chatRoom)
                if (chatRoom.length >0) {
                    chatRooms.push(chatRoom[0]);
                }

                processed++;
                if(processed === club_Code.length) {
                    res.json(chatRooms);
                    console.log("채팅방 조회 :",chatRooms)
                }
            })
        })
    })
})

module.exports = router;