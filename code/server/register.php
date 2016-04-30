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

    $db = Db::getInstance();
    $connect = $db->connect();
    $md5 = md5($password);
    $sql = "insert into User(id, password, level, nickname) values('$id', '$md5', 1, 'Naive')";
    if(!mysql_query($sql, $connect)){
        $errorMsg = mysql_error();
        $head = array(
            "code" => "001",
            "msg" => "注册失败"
        );
        $response = array(
            "head" => $head
        );
        echo json_encode($response);
    }
    else {
        $head = array(
            "code" => "000",
            "msg" => "注册成功"
        );
        $response = array(
            "head" => $head
        );
        echo json_encode($response);
    }
}
register($_POST);
?>
