<?php
include('db.php');

#  @brief 注册传递来的用户名和密码
#  @param arr 字典类型 
function register($arr) {
    $request = $arr['request'];
    #参数true表示返回一个array
    $deJson = json_decode($request, true);
    $para = $deJson['para'];
    $id = $para['id'];
    $password = $para['password'];
    $rePassword = $para['rePassword'];
    if($password != $rePassword){
        return ;
    }

    $db = Db::getInstance();
    $connect = $db->connect();
    $md5 = md5($password);
    $sql = "insert into User(id, password) values('$id', '$md5')";
    if(!mysql_query($sql, $connect)){
        die('Error: ' . mysql_error());
    }
}

?>
