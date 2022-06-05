drop table if exists `blog`;
create table `blog` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment 'name',
  `category` bigint comment 'category',
  `description` varchar(200) default 'I just moved to San Francisco last week from London, so I can provide some perspective.' comment 'description',
  `cover` varchar(200) default 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png' comment 'cover',
  `view_count` int not null default 0 comment 'view_count',
  `vote_count` int not null default 0 comment 'vote_count',
  `user` bigint not null comment 'userId',

  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blog';

insert into `blog` (id, name,category,user) values (1, 'Spring Boot 入门教程',202,1);
insert into `blog` (id, name,category,user) values (2, 'Vue 入门教程',500,1);
insert into `blog` (id, name,category,user) values (3, 'Python 入门教程',300,1);
insert into `blog` (id, name,category,user) values (4, 'Mysql 入门教程',401,1);
insert into `blog` (id, name,category,user) values (5, 'Oracle 入门教程',500,1);
insert into `blog` (id, name,category,user) values (6, 'LeetCode001',201,1);
insert into `blog` (id, name,category,user) values (7, 'LeetCode123',201,1);


# 分类
drop table if exists `category`;
create table `category` (
    `id` bigint not null comment 'id',
    `parent` bigint not null default 0 comment 'parent',
    `name` varchar(50) not null comment 'name',
    `sort` int comment 'sort',
    `user` bigint not null comment 'userId',

    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='category';

insert into `category` (id, parent, name, sort,user) values (200, 000, 'Java', 200,1);
insert into `category` (id, parent, name, sort,user) values (201, 200, 'LeetCode', 201,1);
insert into `category` (id, parent, name, sort,user) values (202, 200, 'Spring', 202,1);
insert into `category` (id, parent, name, sort,user) values (300, 000, 'Python', 300,1);
insert into `category` (id, parent, name, sort,user) values (301, 300, 'Crawler', 301,1);
insert into `category` (id, parent, name, sort,user) values (302, 300, 'Machine Learning', 302,1);
insert into `category` (id, parent, name, sort,user) values (400, 000, 'DataBase', 400,1);
insert into `category` (id, parent, name, sort,user) values (401, 400, 'MySQL', 401,1);
insert into `category` (id, parent, name, sort,user) values (500, 000, 'Other', 500,1);

-- blog content
drop table if exists `content`;
create table `content` (
    `id` bigint not null comment 'blogId',
    `mdContent` mediumtext comment 'mdContent',
    `htmlContent` mediumtext comment 'htmlContent',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blogContent';

-- User
drop table if exists `BlogUser`;
drop table if exists `bloguser`;
create table `BlogUser` (
    `id` bigint not null comment 'ID',
    `login_name` varchar(50) not null comment 'login_name',
    `name` varchar(50) comment 'name',
    `password` char(32) not null comment 'password',
    primary key (`id`),
    unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='BlogUser';
insert into `BlogUser` (id, login_name,name,password) values (1,'test','test','1168982d7f8e34c25387587b59432d9f');
