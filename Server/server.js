const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const cors = require('cors');

const appMainRouter = require('./routes/appMain');
const clubMainRouter = require('./routes/clubMain');
const scheduleRouter = require('./routes/schedule')
const userRouter = require('./routes/user');

app.use(cors())

app.set('port', process.env.PORT || 3333);

app.use(bodyParser.urlencoded({ extended: true }))
app.use('/', appMainRouter)
app.use('/', clubMainRouter)
app.use('/', scheduleRouter)
app.use('/uploads', express.static("C:/Users/gjaischool/Desktop/final_project/Senimo/Server/uploads"));
app.use('/', userRouter)

app.listen(app.get('port'), () => {
  console.log(app.get('port'), '번 포트에서 대기중..');
})