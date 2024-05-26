create table if not exists roles
(
    id bigserial primary key,
    version integer not null,
    created_by bigint,
    created_date timestamp(6),
    updated_by bigint,
    updated_date timestamp(6),
    code text unique,
    is_active boolean,
    name text unique
);

create table if not exists users
(
    id bigserial primary key,
    version integer not null,
    created_by bigint references users,
    created_date timestamp(6),
    updated_by bigint references users,
    updated_date timestamp(6),
    first_name varchar(100),
    last_name varchar(100),
    login varchar(100) unique,
    password varchar(100),
    phone varchar(30),
    email varchar(100) unique,
    city varchar(100),
    avatar text,
    sex varchar(10),
    user_type text,
    last_login timestamp(6),
    blocked boolean,
    strikes_count integer
);

create table if not exists user_roles
(
    id bigserial primary key,
    version integer not null,
    created_by bigint references users,
    created_date timestamp(6),
    updated_by bigint references users,
    updated_date timestamp(6),
    user_id bigint not null references users,
    role_id bigint not null references roles
);

create table if not exists chat
(
    id bigserial primary key,
    sender_id bigint not null,
    receiver_id bigint,
    unique_id text
);

create table if not exists messages
(
    id bigserial primary key,
    version integer not null,
    created_by bigint references users,
    created_date timestamp(6),
    updated_by bigint references users,
    updated_date timestamp(6),
    sender_id bigint not null references users,
    receiver_id bigint not null references users,
    message_text text not null,
    chat_unique_id text,
    status text
);

create table if not exists verification_code
(
    id bigserial primary key,
    version integer not null,
    created_by bigint references users,
    created_date timestamp(6),
    updated_by bigint references users,
    updated_date timestamp(6),
    email varchar(100),
    phone varchar(30),
    code varchar(20) not null
);

alter table roles
    add constraint created_by_fkey foreign key (created_by) references users,
    add constraint updated_by_fkey foreign key (updated_by) references users;