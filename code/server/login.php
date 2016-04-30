<?php
include('db.php');
function login($arr) {
    $request = $arr['request'];
    #参数true表示返回一个array
    $deJson = json_decode($request, true);
    $para = $deJson['para'];
    $id = $para['id'];
    $password = $para['password'];
    $md5 = md5($password);

    $db = Db::getInstance();
    $connect = $db->connect();
    $sql = "select * from User where id = '$id' and password = '$md5'";
    $result = mysql_query($sql, $connect);
    #login success
    if ($row = mysql_fetch_array($result)) {
        $head = array(
            "code" => "003",
            "msg" => "登录成功"
        );
        $para = array(
            "id" => $row['id'],
            'allDist' => $row['allDist'],
            'location' => $row['location'],
            'nickname' => $row['nickname'],
            'sex' => $row['sex'],
            'age' => $row['age'],
            'catFood' => $row['catFood'],
            'catExp' => $row['catExp'],
            'allTime' => $row['allTime'],
            'maxDist' => $row['MaxDist'],
            'maxTime' => $row['MaxTime'],
            'level' => $row['level']
        );
        $response = array(
            'head' => $head,
            'para' => $para
        );
        echo json_encode($response);
    }
    else {
        $head = array(
            "code" => "002",
            "msg" => "登录失败"
        );
        $response = array(
            "head" => $head
        );
        echo json_encode($response);
    }
}
login($_POST);
?>
