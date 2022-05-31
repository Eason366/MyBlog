drop table if exists `blog`;
create table `blog` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment 'name',
  `category` bigint comment 'category',
  `description` varchar(200) default 'I just moved to San Francisco last week from London, so I can provide some perspective.' comment 'description',
  `cover` varchar(200) default 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png' comment 'cover',
  `view_count` int not null default 0 comment 'view_count',
  `vote_count` int not null default 0 comment 'vote_count',

  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blog';

insert into `blog` (id, name,category) values (1, 'Spring Boot 入门教程',202);
insert into `blog` (id, name,category) values (2, 'Vue 入门教程',500);
insert into `blog` (id, name,category) values (3, 'Python 入门教程',300);
insert into `blog` (id, name,category) values (4, 'Mysql 入门教程',401);
insert into `blog` (id, name,category) values (5, 'Oracle 入门教程',500);
insert into `blog` (id, name,category) values (6, 'LeetCode001',201);
insert into `blog` (id, name,category) values (7, 'LeetCode123',201);


# 分类
drop table if exists `category`;
create table `category` (
    `id` bigint not null comment 'id',
    `parent` bigint not null default 0 comment 'parent',
    `name` varchar(50) not null comment 'name',
    `sort` int comment 'sort',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='category';

insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, 'LeetCode', 201);
insert into `category` (id, parent, name, sort) values (202, 200, 'Spring', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort) values (301, 300, 'Crawler', 301);
insert into `category` (id, parent, name, sort) values (302, 300, 'Machine Learning', 302);
insert into `category` (id, parent, name, sort) values (400, 000, 'DataBase', 400);
insert into `category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `category` (id, parent, name, sort) values (500, 000, 'Other', 500);

-- blog content
drop table if exists `content`;
create table `content` (
    `id` bigint not null comment 'blogId',
    `mdContent` mediumtext comment 'mdContent',
    `htmlContent` mediumtext comment 'htmlContent',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blogContent';

