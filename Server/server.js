
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const cors = require('cors');
const http = require('http');
const server = http.createServer(app);
const socketIo = require('socket.io');

const authController = require('./token/authController')
const appMainRouter = require('./routes/appMain');
const clubMainRouter = require('./routes/clubMain');
const scheduleRouter = require('./routes/schedule')
const userRouter = require('./routes/user');
const createMeeting = require('./routes/createMeeting')
const modifyMeeting = require('./routes/modifyMeeting')
const updateInterestedClub = require('./routes/UpdateInterestedClub')
const getAllMembers = require('./routes/getAllMembers')
const updateMember = require('./routes/updateMember')
const updateLeader = require('./routes/updateLeader')
const deleteMember = require('./routes/deleteMember')
const getSchedules = require('./routes/getSchedules')
const getScheduleMembers = require('./routes/getScheduleMembers')
const boardRouter = require('./routes/board')
const getCombinedData = require('./routes/getCombinedData')
const editMyProfile = require('./routes/editMyProfile')
const galleryRouter = require('./routes/gallery')
const initialInterestedClub = require('./routes/initialInterestedClub')
const updatePosts = require('./routes/board')
const chatRoom = require('./routes/chatRoom')
const getUserRole = require('./routes/getUserRole')
const myPageRouter = require('./routes/myPage')
// const chatRouter = require('./routes/chat')


app.use(cors())

//app.set('port', process.env.PORT || 5555);
//ngrok tunnel --label edge=edghts_2YKdAEOOgOIr0zDkBYxQMo8mcyg http://localhost:80

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }))
app.use(express.json()); // JSON 본문을 파싱하기 위한 설정
app.use(express.urlencoded({ extended: true })); // URL 인코딩된 데이터를 파싱하기 위한 설정
app.use('/uploads', express.static("C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads")); // 도운 이미지 경로
// app.use('/uploads', express.static("C:/Users/gjaischool1/Desktop/Final_Project/Senimo/Server/uploads")); // 지혜 이미지 경로
//app.use('/uploads', express.static("C:/Users/aischool/Desktop/Senimo/Server/uploads")); // 희준 이미지 경로

app.use('/', authController)
app.use('/', appMainRouter)
app.use('/', clubMainRouter)
app.use('/', scheduleRouter)
app.use('/', userRouter)
app.use('/', createMeeting)
app.use('/', modifyMeeting)
app.use('/', updateInterestedClub)
app.use('/', getAllMembers)
app.use('/', updateMember)
app.use('/', updateLeader)
app.use('/', deleteMember)
app.use('/', getSchedules)
app.use('/', getScheduleMembers)
app.use('/', deleteMember)
app.use('/', boardRouter)
app.use('/', galleryRouter)
app.use('/', updatePosts)
app.use('/', getCombinedData)
app.use('/', editMyProfile)
app.use('/', galleryRouter)
app.use('/', initialInterestedClub)
app.use('/', chatRoom)
app.use('/', getUserRole)
app.use('/', myPageRouter)
// app.use('/', chatRouter)

// app.listen(app.get('port'), () => {
//   console.log(app.get('port'), '번 포트에서 대기중..');
// })

const io = socketIo(server);

io.on('connection', (socket) => {
    console.log('New client connected');

    socket.on('joinRoom', (roomId) => {
      socket.join(roomId);
      console.log('roomId 입니다',roomId);
  });

  socket.on('chat message', (roomId, message) => {
      io.to(roomId).emit('chat message', message);
      console.log('message 입니다',message);
  });

// // 클라이언트로부터 메시지 수신
// socket.on('new message', (message) => {
//     // 받은 메시지를 다른 클라이언트에게 전송
//     console.log('Received message:', message);

//     // 모든 클라이언트에 메시지 송신
//     io.emit('chat message', message);
// });


    socket.on('disconnect', () => {
        console.log('Client disconnected');
    });
});

const PORT = process.env.PORT || 5555;
server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});