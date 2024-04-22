create table admins (
       id int(11) primary key auto_increment not null,
       username varchar(50) unique not null,
       password varchar(50) not null,
       fullname varchar(50),
       role int(2)
);

insert into admins (username, password, fullname, role) values ('user1', 'password1', 'fullname', 0);

create table settings(
       id int(11) primary key auto_increment not null,
       name varchar(100),
       value varchar(100)
);


insert into settings (name, value) values ('outerPrinter', '');
insert into settings (name, value) values ('innerPrinter', '');
insert into settings (name, value) values ('innerPrinter2', '');
