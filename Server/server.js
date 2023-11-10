const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const cors = require('cors');

const appMainRouter = require('./routes/appMain');

app.use(cors())

app.set('port', process.env.PORT || 3333);

app.use(bodyParser.urlencoded({ extended: true }))
app.use('/', appMainRouter)

app.listen(app.get('port'), () => {
  console.log(app.get('port'), '번 포트에서 대기중..');
})