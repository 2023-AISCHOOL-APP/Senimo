const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.post('/writePost', (req, res) => {
  console.log('writePost router', req.body);
  const { user_id, club_code, post_content, post_img } = req.body

  const writePostSql = `insert into tb_post (user_id, club_code, post_content, post_img)
    values(?,?,?,?)`

  conn.query(writePostSql, [user_id, club_code, post_content, post_img], (err, rows) => {
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

module.exports = router;