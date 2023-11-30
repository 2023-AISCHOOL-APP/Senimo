const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const conn = require('../config/database');
const config = require('../config/config');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

io.on('connection', (socket) => {
    console.log('New client connected');

// 클라이언트로부터 메시지 수신
socket.on('new message', (message) => {
    // 받은 메시지를 다른 클라이언트에게 전송
    console.log('Received message:', message);

    // 모든 클라이언트에 메시지 송신
    io.emit('chat message', message);
});


    socket.on('disconnect', () => {
        console.log('Client disconnected');
    });
});

server.listen(5000, () => {
    console.log('Listening on port 5555');
});