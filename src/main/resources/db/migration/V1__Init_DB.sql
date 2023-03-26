create table incidents (id bigint generated by default as identity (start with 1), status varchar(255), text varchar(1024), timestamp timestamp, title varchar(255), work_group varchar(255), user_id bigint, primary key (id));
create table user_role (user_id bigint not null, roles varchar(255));
create table users (id bigint generated by default as identity (start with 1), password varchar(255), username varchar(255), primary key (id));
alter table incidents add constraint FKkcwpxf40y1etvwdeqsx19julp foreign key (user_id) references users;
alter table user_role add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users;