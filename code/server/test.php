<?php
include('register.php');
$para = array(
    'id' => 'zhensongyan@gmail.com',
    'password' => '12345',
    'rePassword' => '12345'
);
$request = array(
    'para' => $para
);
$json = array(
    "request" => json_encode($request)
);
register($json);
?>
