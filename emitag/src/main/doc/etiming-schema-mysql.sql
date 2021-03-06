CREATE TABLE arr (
	id int primary key,
	arr char(3),
	name char(32)
);

CREATE TABLE class (
    code varchar(32) not null primary key,
    class varchar(32)
);


CREATE TABLE ecard(
    id int not null primary key,
    ecardno int,
    control int,
    times int,
    orgtimes int,
    nr int,
    timechanged float,
    clocktime float
);

CREATE TABLE mellom (
    mellomid int,
    id int,
    mtime float,
    strtid varchar(12),
    iplace int,
    mintime float,
    mchanged float,
    stasjon int,
    rundenr int,
    serial int,
    timechanged float
);

CREATE TABLE name (
    id int not null primary key,
    kid varchar(13),
    ename varchar(32),
    name varchar(32),
    startno int,
    times varchar(12),
    place int,
    team varchar(10),
    class varchar(32),
    cource int,
    starttime float,
    intime float,
    ecard int,
    pnr int,
    status varchar(1),
    statusmsg varchar(64),
    seed int,
    changed timestamp,
    changedby varchar(10),
    seeding int,
    races int,
    rank int,
    lisens varchar(15),
    ecard2 int,
    timechanged float
);


CREATE TABLE team (
    code varchar(10) not null primary key,
    name varchar(32)
);