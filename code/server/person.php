
<?php

include('db.php');

function person($info){

    $request = $info['request'];
    $info_getter = json_decode($request, true);
    $para = $info_getter['para'];
    
    $Rid = $para['Rid'];
    $id = $para['id'];
    
    $db = Db::getInstance();
    $connect = $db->connect();
    
            
    switch ($Rid) {
        case '001'://修改昵称
            $nick = $para['nickname'];
            $stmt = "UPDATE User SET nickname='$nick' WHERE id='$id'";
            $result = mysql_query($stmt,$connect);
            if(!$result){
                $errorMsg = mysql_error();
                $head = array(
                    "code" => "001",
                    "msg" => "修改失败"
                );
                $response = array(
                    "head" => $head
                );
                echo json_encode($response);
            }else{
                $head = array(
                    "code" => "002",
                    "msg" => "修改成功"
                );
                $response = array(
                    "head" => $head
                );
                echo json_encode($response);
            }
            break;
        case '002':
            $dist = $para['dist'];
            $time = $para['time'];
            
            $stmt = "SELECT * FROM User WHERE id='$id'";
            $result = mysql_query($stmt,$connect);
            if(!$result){
                $errorMsg = mysql_error();
                $head = array(
                    "code" => "003",
                    "msg" => "用户获取失败"
                );
                $response = array(
                    "head" => $head
                );
                echo json_encode($response);
            }else{
                $row = mysql_fetch_array($result);
                $maxtime = $row['MaxTime'];
                $maxdist = $row['MaxDist'];
                $alltime = $row['allTime'];
                $alldist = $row['allDist'];
                
                $maxdist = ($maxdist>$dist)?$maxdist:$dist;
                $maxtime = ($maxtime>$time)?$maxtime:$time;
                $alldist += $dist;
                $alltime += $time;
                
                $stmt = "UPDATE User SET allDist=$alldist,allTime=$alltime,MaxDist=$maxdist,MaxTime=$maxtime WHERE id='$id'";
                $result = mysql_query($stmt,$connect);
                if(!$result){
                    $errorMsg = mysql_error();
                    $head = array(
                        "code" => "004",
                        "msg" => "更新失败"
                    );
                    $response = array(
                        "head" => $head
                    );
                    echo json_encode($response);
                }else{
                    $head = array(
                        "code" => "005",
                        "msg" => "更新成功"
                    );
                    $response = array(
                        "head" => $head
                    );
                    echo json_encode($response);
                }
            }
            break;
        default:
            $stmt = "SELECT catFood, catExp, level, nickname, MaxDist,MaxTime,allDist,allTime FROM User WHERE id='$id'";
            $result = mysql_query($stmt,$connect);
            if(!$result){
                $errorMsg = mysql_error();
                $head = array(
                    "code" => "006",
                    "msg" => "信息获取失败"
                );
                $response = array(
                    "head" => $head
                );
                echo json_encode($response);
            }else{
                $row = mysql_fetch_array($result);
                $head = array(
                    "code" => "007",
                    "msg" => "信息获取成功",
                    "catFood" => $row['catFood'],
                    "catExp" => $row['catExp'],
                    "level" => $row['level'],
                    "nickname" => $row['nickname'],
                    "MaxTime" => $row['MaxTime'],
                    "MaxDist" => $row['MaxDist'],
                    "allTime" => $row['allTime'],
                    "allDist" => $row['allDist']
                );
                $response = array(
                    "head" => $head
                );
                echo json_encode($response);
            }
            break;
    }
}

person($_POST);
?>
