create table rooms
(
	id serial not null
		constraint room_pk
			primary key,
	room_key varchar(40)
);
