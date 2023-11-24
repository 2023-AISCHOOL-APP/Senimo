// jwt.js
const jwt = require('jsonwebtoken');

const ACCESS_TOKEN_SECRET = 'your_access_token_secret';
const REFRESH_TOKEN_SECRET = 'your_refresh_token_secret';

exports.generateTokens = (user) => {
    console.log("jwt",user)
    const accessToken = jwt.sign({ userId: user.userId }, ACCESS_TOKEN_SECRET, { expiresIn: '15m' });
    const refreshToken = jwt.sign({ userId: user.userId }, REFRESH_TOKEN_SECRET, { expiresIn: '7d' });
    return { accessToken, refreshToken };
};

exports.verifyToken = (token, type='access') => {
    try {
        const secret = type === 'access' ? ACCESS_TOKEN_SECRET : REFRESH_TOKEN_SECRET;
        return jwt.verify(token, secret);
    } catch (error) {
        return null;
    }
};
