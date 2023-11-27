const mysql = require('mysql2');

function connectDatabase() {
    const conn = mysql.createConnection({
        host: 'project-db-stu3.smhrd.com',
        user: 'Insa4_App_final_3',
        password: 'aischool3',
        port: 3307,
        database: 'Insa4_App_final_3'
    });

    conn.connect(error => {
        if (error) {
            console.error("데이터베이스 연결 실패: " + error.stack);
            setTimeout(connectDatabase, 5000); // 2초 후 재연결 시도
        } else {
            console.log('데이터베이스에 연결되었습니다.');
        }
    });

    conn.on('error', error => {
        if (error.code === 'PROTOCOL_CONNECTION_LOST') {
            console.error('데이터베이스 연결이 끊어졌습니다. 재연결을 시도합니다.');
            connectDatabase();
        } else {
            throw error;
        }
    });

    return conn;
}

const connection = connectDatabase();

module.exports = connection;


// const mysql = require('mysql2/promise');

// async function initializeDatabase() {
//     const pool = mysql.createPool({
//         host: 'project-db-stu3.smhrd.com',
//         user: 'Insa4_App_final_3',
//         password: 'aischool3',
//         port: 3307,
//         database: 'Insa4_App_final_3',
//         waitForConnections: true,
//         connectionLimit: 10,
//         queueLimit: 0
//     });

//     // 데이터베이스 연결 성공 로그
//     pool.getConnection()
//         .then(conn => {
//             console.log('데이터베이스에 연결되었습니다.');
//             conn.release(); // 연결을 풀에 반환
//         })
//         .catch(err => {
//             if (err.code === 'PROTOCOL_CONNECTION_LOST') {
//                 console.error('데이터베이스 연결이 끊어졌습니다. 재연결을 시도합니다.');
//             } else {
//                 console.error('데이터베이스 연결 실패: ', err);
//             }
//         });

//     return pool;
// }

// const dbPool = initializeDatabase();

// module.exports = dbPool;



// const mysql = require('mysql2')

// let conn = mysql.createConnection({
//   'host': 'project-db-stu3.smhrd.com',
//   'user': 'Insa4_App_final_3',
//   'password': 'aischool3',
//   'port': 3307,
//   'database': 'Insa4_App_final_3'
// })



// conn.connect(error => {
//   if (error) {
//     console.error("데이터베이스 연결 실패: " + error.stack);
//     setTimeout(connectDatabase, 2000); // 2초 후 재연결 시도
//   }
//   console.log('데이터베이스에 연결되었습니다.');
// });

// // 내 mysql 정보를 가지고 연결한 conn을 모듈화
// module.exports = conn;