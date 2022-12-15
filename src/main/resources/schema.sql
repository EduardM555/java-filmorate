create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(255) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(255) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(255) not null,
    DESCRIPTION  CHARACTER VARYING(255) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA_ID       CHARACTER VARYING(255) not null,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID       INTEGER not null,
    GENRE_ID      INTEGER not null,
    FILM_GENRE_ID INTEGER auto_increment,
    constraint "FILMS_GENRES_pk"
        primary key (FILM_GENRE_ID),
    constraint "FILMS_GENRES_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILMS_GENRES_GENRES_null_fk"
        foreign key (GENRE_ID) references GENRES
);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    EMAIL     CHARACTER VARYING(255) not null
        unique,
    LOGIN     CHARACTER VARYING(255) not null
        unique,
    USER_NAME CHARACTER VARYING(255) not null,
    BIRTHDAY  DATE                   not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDSHIP
(
    FRIENDSHIP_ID INTEGER auto_increment,
    FRIEND_ID     INTEGER not null,
    USER_ID       INTEGER not null,
    constraint "FRIENDSHIP_pk"
        primary key (FRIENDSHIP_ID),
    constraint "FRIENDSHIP_FRIENDS_null_fk"
        foreign key (FRIEND_ID) references USERS,
    constraint "FRIENDSHIP_USERS_null_fk"
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS LIKES
(
    FILM_ID  INTEGER not null,
    USER_ID  INTEGER not null,
    LIKES_ID INTEGER auto_increment,
    constraint LIKES_PK
        primary key (LIKES_ID),
    constraint "FILMS_LIKES_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILMS_LIKES_USERS_null_fk"
        foreign key (USER_ID) references USERS
);