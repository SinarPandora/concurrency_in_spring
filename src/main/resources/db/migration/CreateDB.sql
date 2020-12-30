create table person
(
    id               serial       not null
        constraint user_pk
            primary key,
    name             varchar(64)  not null
        constraint person_uni_name
            unique,
    email            varchar      not null,
    age              integer      not null,
    gender           integer      not null,
    address
                     varchar      not null default 'Not set yet',
    is_active        boolean      not null default true,
    last_update_date timestamp(3) not null default current_timestamp
);
