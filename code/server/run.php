<?php
include('db.php');
function run($arr) {
    $request = $arr['request'];
    $request = json_decode($request, true);
    $para = $request['para'];
    $db = Db::getInstance();
    $connect = $db->connect();
    $dist = $para['dist'];
    $id = $para['id'];
    $time = $para['time'];
    $sql = "update User set allTime=allTime+$time, allDist=allDist+$dist, catFood=catFood+$dist*10 where id='$id'";
    $result = mysql_query($sql, $connect);
    $sql = "update User set MaxTime=$time where id='$id' and MaxTime<$time";
    mysql_query($sql, $connect);
    $sql = "update User set MaxDist=$dist where id='$id' and MaxDist<$dist";
    mysql_query($sql, $connect);
}
run($_POST);
?>
