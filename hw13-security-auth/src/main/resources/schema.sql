create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments (
    id bigserial,
    s_text varchar(1000),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

create table users(
    id bigserial,
	username varchar(50) not null,
	password varchar(500) not null,
	enabled boolean not null,
	primary key (id)
);

create table authorities (
    id bigserial,
	authority varchar(50) not null,
	primary key (id)
);

create table users_authorities (
        user_id bigint references users(id) on delete cascade,
        authority_id bigint references authorities(id) on delete cascade,
        primary key (user_id, authority_id)
)