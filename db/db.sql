CREATE DATABASE quarkus-social;

CREATE TABLE USERS (
	ID BIGSERIAL NOT NULL PRIMARY KEY,
	NAME VARCHAR(100) NOT NULL,
	AGE INTEGER NOT NULL
);

CREATE TABLE POSTS (
id BIGSERIAL NOT NULL PRIMARY KEY,
post_content TEXT NOT NULL,
dateTime timestamp not null,
user_id bigint NOT NULL REFERENCES USERS(id)
);

CREATE TABLE followers (
id BIGSERIAL NOT NULL PRIMARY KEY,
user_id BIGINT not null,
follower_id BIGINT not null
)