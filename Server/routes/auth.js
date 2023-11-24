const conn = require('../config/database');

async function normalAuth(req,res,next){
  let userToken = req.headers['authorization'];
  console.log("토큰 검증해주세요",userToken)
  if(!userToken){
    // throw new Error('not logged in');
    return res.status(403).json({
      success: false,
      message: 'not logged in'
    })
  }else{
    userToken = userToken.split(' ')[1];
  }

  try{
    console.log("userToken입니다 :",userToken)
    conn.query(`select * from tb_user where user_accessToken=?`,[userToken],(err,rows)=>{
        console.log("rows는 담겼습니다.",rows)
        console.log("err는 담겼습니다.",err)
      try{
        if(err) throw err;
        if(rows.length === 0){
          throw new Error('not logged in');
        }
        
        const result = {
          user_id: rows[0].user_id,
          user_name: rows[0].user_name,
          birth_year: rows[0].birth_year,
          gender: rows[0].gender,
          user_gu: rows[0].user_gu,
          user_dong: rows[0].user_dong,
          user_introduce: rows[0].user_introduce,
          user_img:rows[0].user_img
        }
        console.log("result는 담겼습니다.",result)
        res.locals.userInfo = result
        return next();
      }catch(err){
        return res.json({
          success: false,
          message: err.message
        })
      }
    });
  }catch(err){
    return res.status(403).json({
      success: false,
      message: err.message
    });
  }
}

module.exports={
  normalAuth
}