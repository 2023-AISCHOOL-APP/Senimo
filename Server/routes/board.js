const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')
const multer = require('multer');
const { v4: uuidv4 } = require('uuid');

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

// 게시물 등록
router.post('/writePost', upload.single('picture') ,(req, res) => {
  console.log('writePost router', req.body);
  const writePost = JSON.parse(req.body.writePostResVO)
  const { user_id, club_code, post_content} = writePost

  // 파일이 업로드된 경우, 파일명을 사용
  const postImgFilename = req.file ? req.file.filename : null;

  const writePostSql = `insert into tb_post (user_id, club_code, post_content, post_img)
    values(?,?,?,?)`

  conn.query(writePostSql, [user_id, club_code, post_content, postImgFilename], (err, rows) => {
    console.log('게시글 : ', rows);
    if (err) {
      console.error('게시글 등록 실패 : ', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('게시글 등록 성공');
      res.json({ rows: 'success' });
    }
  });
})

// 게시글 리스트 가져오기
router.get('/getPostContent/:club_code', (req, res) => {
  console.log('getPostContent', req.body);
  let club_code = req.params.club_code

  const getPostSql = `
    select tp.post_code, tu.user_img, tu.user_name, tp.created_dt, tp.post_content, tp.post_img, tj.club_role, count(tr.review_code) as review_count
    from tb_post tp
    left join tb_user tu on tp.user_id = tu.user_id
    left join tb_join tj on tp.club_code = tj.club_code and tu.user_id = tj.user_id
    left join tb_review tr on tp.post_code = tr.post_code
    where tp.club_code = ?
    group by tp.post_code, tu.user_img, tu.user_name, tp.created_dt, tp.post_content, tp.post_img, tj.club_role;
  `

  conn.query(getPostSql, [club_code], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    if (results.length > 0) {
        console.log("안드로이드로 보내는 post content 값", results);
        res.status(200).json({ data: results });
    } else {
        res.status(404).json({ error: "post content not found." });
    }
  });
})

// 댓글 리스트 가져오기
router.get('/getReview/:post_code', (req, res) => {
  console.log('getReview', req.body);
  let post_code = req.params.post_code

  const getReviewSql = `
    select b.user_img, b.user_name, a.review_content, a.reviewed_dt
    from tb_review a
    left join tb_user b on a.user_id = b.user_id
    where a.post_code = ?;
  `

  conn.query(getReviewSql, [post_code], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }

    if (results.length > 0) {
        console.log("안드로이드로 보내는 review content 값", results);
        res.status(200).json({ data: results });
    } else {
        res.status(404).json({ error: "review content not found." });
    }
  });
})

// 게시글 삭제
router.post('/deletePost', (req, res) => {
  console.log('게시글 삭제 라우터', req.body);
  const { post_code } = req.body;

  const deletePostSql = `delete from tb_post where post_code = ?`

  conn.query(deletePostSql, [ post_code ], (err, rows) => {
    if (err) {
      console.error('게시글 삭제 실패', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('게시글 삭제 성공');
      res.json({ rows: 'success' });
    }
  })
})

module.exports = router;