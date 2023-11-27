const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const cors = require('cors');

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


app.use(cors())

app.set('port', process.env.PORT || 3333);
//ngrok tunnel --label edge=edghts_2YKdAEOOgOIr0zDkBYxQMo8mcyg http://localhost:80
//app.set('port', process.env.PORT || 80);

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }))
app.use('/uploads', express.static("C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads"));
app.use('/', authController)
app.use('/', appMainRouter)
app.use('/', clubMainRouter)
app.use('/', scheduleRouter)
app.use('/', userRouter)
app.use('/',createMeeting)
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
app.use('/', getCombinedData)
app.use('/', editMyProfile)
app.use('/', galleryRouter)

app.listen(app.get('port'), () => {
  console.log(app.get('port'), '번 포트에서 대기중..');
})