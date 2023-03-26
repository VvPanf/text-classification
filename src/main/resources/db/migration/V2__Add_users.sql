insert into users (username, password) values
('admin', 'admin'),
('user', 'user');

insert into user_role (user_id, roles) values
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');