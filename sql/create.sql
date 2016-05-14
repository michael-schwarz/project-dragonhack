CREATE DATABASE potato
 OWNER =  "Nick"
 ENCODING = 'UTF8'
 CONNECTION LIMIT = -1
 
\c potato

CREATE SCHEMA dh

CREATE TABLE dh.university
(
	university_id integer PRIMARY KEY NOT NULL,
	name char[100] NOT NULL,
	country char[100] NOT NULL
	
)

CREATE TABLE dh.users
(
	user_id serial PRIMARY KEY NOT NULL,
	nickname char[20] unique NOT NULL,
	device_id uuid unique NOT NULL,
	university_id integer NOT NULL REFERENCES dh.university(university_id),
	day_all integer,
	day_slack integer,
	month_all integer,
	month_slack integer,
	total_all integer,
	total_slack integer
);