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

router.post('/writePost', upload.single('picture') ,(req, res) => {
  console.log('writePost router', req.body);
  const writePost = JSON.parse(req.body.writePostResVO)
  const { user_id, club_code, post_content} = writePost

  // 파일이 업로드된 경우, 파일명을 사용
  const postImgFilename = req.file ? req.file.filename : null;

  const writePostSql = `insert into tb_post (user_id, club_code, post_content, post_img)
    values(?,?,?,?)`

  conn.query(writePostSql, [user_id, club_code, post_content, req.file.postImgFilename], (err, rows) => {
    console.log('게시글 : ', rows);
    if (err) {
      console.error('게시글 등록 실패 : ', err);
      res.json({ rows: 'failed' });
    } else {
      console.log('게시글 등록 성공');
      res.json({ rows: 'success' });
      console.log(success)
    }
  });
})

module.exports = router;