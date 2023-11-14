package com.example.senimoapplication.Club.VO
import java.sql.Timestamp

class PostVO (
    // tb_user에서 가져올 정보 - 작성자 정보(회원이름, 프로필사진)
    val user_name : String ="" ,
    val user_img : String ="",

    // tb_join - 작성자 역할
    val club_role : Int= 0,

    // tb_post 게시물 정보(작성 일시, 게시글 내용)
    val created_dt : String ="",
    val post_content : String ="",

    // tb_post_img
    val img_name : String ="",

    // tb_review - 댓글 정보 (내용, 일시, 댓글 수, 댓글 작성자 정보)
    val review_content : ArrayList<CommentVO>,
    val reviewed_cnt : Int = 0


)