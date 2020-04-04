create table users
(
    id serial not null
        constraint users_pk
            primary key,
    name varchar(255) not null,
    session_key varchar(255) not null,
    room_id int
        constraint users_rooms_id_fk
            references rooms
);
