create database if not exists RunningCat;
use RunningCat;
drop table if exists Running;
drop table if exists User;
create table User(
    age         integer,
    height      integer,
    weight      float, 
    sex         char(1),
    nickname    varchar(20),
    location    varchar(20),
    catExp      integer,
    catFood     integer,
    id          char(50) primary key,
    createDate  date,
    password    varchar(50),
    allDist     float
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
drop table if exists FriendReqest;

CREATE TABLE Friend
(
    lowerID CHAR(50) NOT NULL,
    upperID CHAR(50) NOT NULL,
  
    PRIMARY KEY (lowerID,upperID)
);

CREATE TABLE FriendReqest
(
    sendID CHAR(50) NOT NULL,
    rcvID CHAR(50) NOT NULL,
  
    PRIMARY KEY (sendID,rcvID)
);
