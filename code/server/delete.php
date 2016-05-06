<?php
include('db.php');
function _delete($arr) {
    $request = $arr['request'];
    $request = json_decode($request, true);
    $para = $request['para'];
    $db = Db::getInstance();
    $connect = $db->connect();
    $lowerId = $para['lowerId'];
    $upperId = $para['upperId'];
    $sql = "delete from Friend where lowerID='$lowerId' and upperID='$upperId'";
    mysql_query($sql, $connect);
}
_delete($_POST);
?>
