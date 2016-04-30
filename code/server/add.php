<?php
include('db.php');
function add($arr) {
    $request = $arr['request'];
    $request = json_decode($request, true);
    $para = $request['para'];
    $db = Db::getInstance();
    $connect = $db->connect();
    $sendId = $para['sendId'];
    $rcvId = $para['rcvId'];
    if($sendId) {
        $sql = "insert into FriendRequest(sendID, rcvID) values('$sendId', '$rcvId')";
        if(mysql_query($sql, $connect)) {
            $head = array(
                "code" => "009",
            );
        }
        else {
            $head = array(
                'code' => '008'
            );
        }
        $response = array(
            'head' => $head
        );
        echo json_encode($response);
    }
}
add($_POST);
?>
