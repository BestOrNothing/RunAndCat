create database if not exists RunningCat;
use RunningCat;
drop table if exists Running;
drop table if exists User;
create table User(
    id          char(50) primary key,
    password    varchar(50),
    nickname    varchar(20),
    location    varchar(20),
    catExp      integer NOT NULL DEFAULT 0,
    catFood     integer NOT NULL DEFAULT 0,
    allDist     float NOT NULL DEFAULT 0,
    allTime     float NOT NULL DEFAULT 0,
    MaxDist     float NOT NULL DEFAULT 0,
    MaxTime     float NOT NULL DEFAULT 0,
    level       integer 
);
drop table if exists Day;
create table Day(
    date        date primary key   
);
create table Running(
    id          char(50), 
    date        date,     
    dist        float,
    foreign key(date) references Day(date),
    foreign key(id) references User(id)
);

drop table if exists Friend;
drop table if exists FriendRequest;

CREATE TABLE Friend
(
    lowerID CHAR(50) NOT NULL,
    upperID CHAR(50) NOT NULL,
  
    PRIMARY KEY (lowerID,upperID)
);

CREATE TABLE FriendRequest
(
    sendID CHAR(50) NOT NULL,
    rcvID CHAR(50) NOT NULL,
  
    PRIMARY KEY (sendID,rcvID)
);
