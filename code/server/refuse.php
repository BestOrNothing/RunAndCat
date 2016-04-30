<?php
include('db.php');
function refuse($arr) {
    $request = $arr['request'];
    $request = json_decode($request, true);
    $para = $request['para'];
    $db = Db::getInstance();
    $connect = $db->connect();
    $lowerId = $para['lowerId'];
    $upperId = $para['upperId'];
    $sql = "delete from FriendRequest where (sendID='$lowerId' and rcvID='$upperId') or (sendID='$upperId' and rcvID='$lowerId')";
    mysql_query($sql, $connect);
}
refuse($_POST);
?>
