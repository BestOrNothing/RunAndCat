<?php
include('db.php');
function getFriend($arr) {
    $request = $arr['request'];
    $request = json_decode($request, true);
    $para = $request['para'];
    $db = Db::getInstance();
    $connect = $db->connect();
    $id = $para['id'];
    $friendId = $para['friendId'];
    if($friendId) {
        $sql = "select * from User where id = '$friendId'";
        $result = mysql_query($sql, $connect);
        if(!result) {
            die(mysql_error());
        }
    }
    else {
        $sql = "select * from User u, Friend f where f.lowerID='$id' and f.upperID=u.id union select * from User u, Friend f where f.upperID='$id' and f.lowerID=u.id";
        $result = mysql_query($sql, $connect);
        if(!result) die(mysql_error());
    }
    $head = array(
        'code' => '007'
    );
    $friendlist = array();
    $count = 0;
    while($row = mysql_fetch_array($result)) {
        array_push($friendlist, $row['nickname'], $row['location'], $row['level'], $row['id']);
        $count ++;
    }
    $head['friendlist'] = $friendlist;
    $head['count'] = $count;
    $response = array(
        'head' => $head
    );
    echo json_encode($response);
}
getFriend($_POST);
?>
