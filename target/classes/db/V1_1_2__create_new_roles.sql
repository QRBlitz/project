insert into roles (version, created_date, code, is_active, name)
values (0, now(), 'ADMIN', true, 'Администратор'),
       (0, now(), 'USER', true, 'Пользователь'),
       (0, now(), 'HELP_DESK', true, 'Служба поддержки');