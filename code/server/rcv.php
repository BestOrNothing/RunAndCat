<?php
include('db.php');
function rcv($arr) {
    $request = $arr['request'];
    $deJson = json_decode($request, true);
    $para = $deJson['para'];
    $id = $para['id'];
    $db = Db::getInstance();
    $connect = $db->connect();
    $sql = "select * from User u, FriendRequest f where u.id=f.sendID and f.rcvID='$id'";
    $result = mysql_query($sql, $connect);
    $head = array();
    $count = 0;
    $friend = array();
    while ($row = mysql_fetch_array($result)) {
        array_push($friend, $row['nickname'], $row['location'], $row['level'], $row['id']);
        $count++;
    }
    $head['friend'] = $friend;
    $head['count'] = $count;
    $response = array(
        'head' => $head,
    );
    echo json_encode($response);
}
rcv($_POST);
?>
