const { generateTokens, verifyToken } = require('./jwt');
const express = require('express')
const router = express.Router()

// ...

// 리프레시 토큰을 사용하여 새로운 액세스 토큰 발급
router.post('/refreshToken', (req, res) => {
    console.log("authController",req.body);
    const { refreshToken } = req.body;

    const userData = verifyToken(refreshToken, 'refresh');
    if (userData) {
        const newTokens = generateTokens({ userId: userData.userId });
        console.log("newTokens",newTokens);
        res.json(newTokens);
    } else {
        res.status(401).json({ message: 'Invalid refresh token' });
    }
});

module.exports = router;