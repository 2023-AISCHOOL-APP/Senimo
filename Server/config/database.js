const mysql = require('mysql2')

let conn = mysql.createConnection({
  'host': 'project-db-stu3.smhrd.com',
  'user': 'Insa4_App_final_3',
  'password': 'aischool3',
  'port': 3307,
  'database': 'Insa4_App_final_3'
})



conn.connect(error => {
  if (error) {
    console.error("데이터베이스 연결 실패: " + error.stack);
    return;
  }
  console.log('데이터베이스에 연결되었습니다.');
})

// 내 mysql 정보를 가지고 연결한 conn을 모듈화
module.exports = conn;