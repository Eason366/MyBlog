drop table if exists `blog`;
create table `blog` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment 'name',
  `category1_id` bigint comment 'category1_id',
  `category2_id` bigint comment 'category2_id',
  `cover` varchar(200) comment 'cover',
  `view_count` int not null default 0 comment 'view_count',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='blog';

insert into `blog` (id, name) values (1, 'Spring Boot 入门教程');
insert into `blog` (id, name) values (2, 'Vue 入门教程');
insert into `blog` (id, name) values (3, 'Python 入门教程');
insert into `blog` (id, name) values (4, 'Mysql 入门教程');
insert into `blog` (id, name) values (5, 'Oracle 入门教程');

