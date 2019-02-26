create table account
(
	username varchar(20) not null
		primary key,
	password varchar(64) not null,
	salt varchar(64) not null,
	constraint username_UNIQUE
		unique (username)
)
default character set utf8mb4,
engine=InnoDB
;

create table user_info
(
	username varchar(20) not null comment '用户名'
		primary key,
	nickname varchar(16) null comment '昵称',
	icon int default '1' null comment '头像编号',
	reg_time bigint default '0' null comment '注册时间',
	constraint user_info_username_uindex
		unique (username)
)
default character set utf8mb4,
comment '用户信息表' engine=InnoDB
;


create table friend_relation
(
	id int auto_increment comment '自增主键'
		primary key,
	user1 varchar(20) not null,
	user2 varchar(20) not null,
	add_time bigint not null comment '添加时间',
	constraint friend_relation_id_uindex
	unique (id),
	constraint user1
	unique (user1, user2)
)
	comment '好友记录表' engine=InnoDB charset=utf8mb4
;

create index friend_relation_user1_index
	on mycc.friend_relation (user1)
;

create index friend_relation_user2_index
	on mycc.friend_relation (user2)
;

insert into friend_relation(user1, user2, add_time) values ("friend1", "friend2", 123456);
insert into friend_relation(user1, user2, add_time) values ("friend1", "friend3", 123456);
insert into friend_relation(user1, user2, add_time) values ("friend1", "friend4", 123456);
insert into friend_relation(user1, user2, add_time) values ("friend2", "friend3", 123456);

delete from friend_relation where user1 = "friend1" and user2 = "friend4";

