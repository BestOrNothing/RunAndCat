<?php
include('db.php');
function food($arr) {
    $db = Db::getInstance();
    $connect = $db->connect();
    $request = $arr['request'];
    $request = json_decode($request, true);
    $catExp = $request['catExp'];
    $catFood = $request['catFood'];
    $id = $request['id'];
    $level = $request['level'];
    $sql = "update User set level=$level, catExp=$catExp, catFood=$catFood where id='$id'";
    mysql_query($sql, $connect);
}
food($_POST);
?>
