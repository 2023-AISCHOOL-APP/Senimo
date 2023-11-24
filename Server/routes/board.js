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

module.exports = router;