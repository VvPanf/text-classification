drop table incidents if exists;
drop table user_role if exists;
drop table users if exists;
drop sequence hibernate_sequence if exists;
create sequence hibernate_sequence start with 1 increment by 1;
create table incidents (id bigint not null, status varchar(255), text varchar(1024), timestamp timestamp, title varchar(255), work_group varchar(255), user_id bigint, primary key (id));
create table user_role (user_id bigint not null, roles varchar(255));
create table users (id bigint not null, password varchar(255), username varchar(255), primary key (id));
alter table incidents add constraint FKkcwpxf40y1etvwdeqsx19julp foreign key (user_id) references users;
alter table user_role add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users;