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

    socket.on('disconnect', () => {
        console.log('Client disconnected');
    });
});

server.listen(5555, () => {
    console.log('Listening on port 3000');
});