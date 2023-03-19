insert into users (id, username, password) values
(1, 'admin', 'admin'),
(2, 'user', 'user');

insert into user_role (user_id, roles) values
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');