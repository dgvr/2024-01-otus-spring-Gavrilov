insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(s_text, book_id)
values ('Comment1', 1), ('Comment2', 1),
       ('Comment3', 2), ('Comment4', 2),
       ('Comment5', 3), ('Comment6', 3);

insert into users(username, password, enabled)
--values ('us', 'pass', true);
values ('us', '$2a$12$Eztu3Nl7UHEWf.ELwxCopee6VM3zWLCwN18Cf3SDJ8xURIr4WWOam', true),
       ('admin', '$2a$12$Eztu3Nl7UHEWf.ELwxCopee6VM3zWLCwN18Cf3SDJ8xURIr4WWOam', true);

 insert into authorities(authority)
 values('ROLE_USER'),
       ('ROLE_ADMIN');


insert into users_authorities(user_id, authority_id)
values (1, 1),  (2, 2);