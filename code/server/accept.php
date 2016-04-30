<?php
include('db.php');
function accept($arr) {
    $request = $arr['request'];
    $request = json_decode($request, true);
    $para = $request['para'];
    $db = Db::getInstance();
    $connect = $db->connect();
    $lowerId = $para['lowerId'];
    $upperId = $para['upperId'];
    $sql = "insert into Friend(lowerId, upperId) values('$lowerId', '$upperId')";
    mysql_query($sql, $connect);
    $sql = "delete from FriendRequest where (sendID='$lowerId' and rcvID='$upperId') or (sendID='$upperId' and rcvID='$lowerId')";
    mysql_query($sql, $connect);
}
accept($_POST);
?>
