const express = require('express')
const router = express.Router()
const conn = require('../config/database');
const config = require('../config/config')

router.post('/updateMemberRole', (req, res) => {
  console.log('result', req.body);
  const { club_code, user_id } = req.body;
  
});

module.exports = router;