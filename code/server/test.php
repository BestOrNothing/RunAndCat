<?php
include('add.php');
$para = array(
    'sendId' => 'zhensongyan@gmail.com',
    'rcvId' => '294173687@qq.com'
);
$request = array(
    'para' => $para
);
$json = array(
    "request" => json_encode($request)
);
#add($json);
?>
