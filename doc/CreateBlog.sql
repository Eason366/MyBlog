drop table if exists `blog`;
create table `blog` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment 'name',
  `category` bigint comment 'category',
  `description` varchar(200) comment 'description',
  `cover` varchar(200) default 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png' comment 'cover',
  `view_count` int not null default 0 comment 'view_count',
  `vote_count` int not null default 0 comment 'vote_count',
  `user` varchar(50) not null comment ' user_login_name',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blog';



# 分类
drop table if exists `category`;
create table `category` (
    `id` bigint not null comment 'id',
    `parent` bigint not null default 0 comment 'parent',
    `name` varchar(50) not null comment 'name',
    `sort` int comment 'sort',
    `user` varchar(50) not null comment 'user_login_name',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='category';

insert into `category` (id, parent, name, sort,user) values (200, 000, 'Java', 200,'test');
insert into `category` (id, parent, name, sort,user) values (201, 200, 'LeetCode', 201,'test');
insert into `category` (id, parent, name, sort,user) values (202, 200, 'Spring', 202,'test');
insert into `category` (id, parent, name, sort,user) values (300, 000, 'Python', 300,'test');
insert into `category` (id, parent, name, sort,user) values (301, 300, 'Crawler', 301,'test');
insert into `category` (id, parent, name, sort,user) values (302, 300, 'Machine Learning', 302,'test');
insert into `category` (id, parent, name, sort,user) values (400, 000, 'DataBase', 400,'test');
insert into `category` (id, parent, name, sort,user) values (401, 400, 'MySQL', 401,'test');
insert into `category` (id, parent, name, sort,user) values (500, 000, 'Other', 500,'test');

-- blog content
drop table if exists `content`;
create table `content` (
    `id` bigint not null comment 'blogId',
    `mdContent` mediumtext comment 'mdContent',
    `htmlContent` mediumtext comment 'htmlContent',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blogContent';

-- User
drop table if exists `bloguser`;
create table `bloguser` (
    `id` bigint not null comment 'ID',
    `login_name` varchar(50) not null comment 'login_name',
    `name` varchar(50) comment 'name',
    `password` char(32) not null comment 'password',
    primary key (`id`),
    unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='bloguser';
insert into `bloguser` (id, login_name,name,password) values (1,'test','test','1168982d7f8e34c25387587b59432d9f');
