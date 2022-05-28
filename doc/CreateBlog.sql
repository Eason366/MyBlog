drop table if exists `blog`;
create table `blog` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment 'name',
  `category1_id` bigint comment 'category1_id',
  `category2_id` bigint comment 'category2_id',
  `description` varchar(200) default 'I just moved to San Francisco last week from London, so I can provide some perspective.' comment 'description',
  `cover` varchar(200) default 'https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png' comment 'cover',
  `view_count` int not null default 0 comment 'view_count',
  `vote_count` int not null default 0 comment 'vote_count',

  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blog';

insert into `blog` (id, name) values (1, 'Spring Boot 入门教程');
insert into `blog` (id, name) values (2, 'Vue 入门教程');
insert into `blog` (id, name) values (3, 'Python 入门教程');
insert into `blog` (id, name) values (4, 'Mysql 入门教程');
insert into `blog` (id, name) values (5, 'Oracle 入门教程');
insert into `blog` (id, name) values (11, 'Spring Boot 入门教程');
insert into `blog` (id, name) values (22, 'Vue 入门教程');
insert into `blog` (id, name) values (32, 'Python 入门教程');
insert into `blog` (id, name) values (42, 'Mysql 入门教程');
insert into `blog` (id, name) values (53, 'Oracle 入门教程');
insert into `blog` (id, name) values (14, 'Spring Boot 入门教程');
insert into `blog` (id, name) values (25, 'Vue 入门教程');
insert into `blog` (id, name) values (36, 'Python 入门教程');
insert into `blog` (id, name) values (47, 'Mysql 入门教程');
insert into `blog` (id, name) values (51, 'Oracle 入门教程');

