SRPS database

-- Database: srps

-- DROP DATABASE srps;

CREATE DATABASE srps
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'English_United Kingdom.1252'
       LC_CTYPE = 'English_United Kingdom.1252'
       CONNECTION LIMIT = -1;


Survey schema

-- Schema: survey

-- DROP SCHEMA survey;

CREATE SCHEMA survey
  AUTHORIZATION postgres;


Authentication schema

-- Schema: authentication

-- DROP SCHEMA authentication;

CREATE SCHEMA authentication
  AUTHORIZATION postgres;



Roles Table 

-- Table: authentication.role

-- DROP TABLE authentication.role;

CREATE TABLE authentication.role
(
  role_id integer NOT NULL,
  role_name character varying,
  CONSTRAINT role_id_pk PRIMARY KEY (role_id),
  CONSTRAINT role_id_name_unique UNIQUE (role_id, role_name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE authentication.role
  OWNER TO postgres;




User Table 

-- Table: authentication."user"

-- DROP TABLE authentication."user";

CREATE TABLE authentication."user"
(
  username character varying NOT NULL, -- wraps email address
  password character varying,
  first_name character varying,
  last_name character varying,
  dob date,
  disabled boolean DEFAULT true,
  CONSTRAINT username_pk PRIMARY KEY (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE authentication."user"
  OWNER TO postgres;
COMMENT ON COLUMN authentication."user".username IS 'wraps email address';



user_role table 


-- Table: authentication.user_role

-- DROP TABLE authentication.user_role;

CREATE TABLE authentication.user_role
(
  username character varying NOT NULL,
  role_id integer NOT NULL,
  CONSTRAINT username_role_id_pk PRIMARY KEY (username, role_id),
  CONSTRAINT role_id_fk FOREIGN KEY (role_id)
      REFERENCES authentication.role (role_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT username_fk FOREIGN KEY (username)
      REFERENCES authentication."user" (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE authentication.user_role
  OWNER TO postgres;
COMMENT ON TABLE authentication.user_role
  IS 'many to many relationship table between user and role';



blank_form table 

-- Table: survey.blank_form

-- DROP TABLE survey.blank_form;

CREATE TABLE survey.blank_form
(
  form_id integer NOT NULL,
  form_name character varying,
  CONSTRAINT form_id_pk PRIMARY KEY (form_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE survey.blank_form
  OWNER TO postgres;



form_user table 

-- Table: survey.form_user

-- DROP TABLE survey.form_user;

CREATE TABLE survey.form_user
(
  form_id integer NOT NULL,
  username character varying NOT NULL,
  CONSTRAINT form_user_pk PRIMARY KEY (form_id, username),
  CONSTRAINT form_id_fk FOREIGN KEY (form_id)
      REFERENCES survey.blank_form (form_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT username_fk FOREIGN KEY (username)
      REFERENCES authentication."user" (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE survey.form_user
  OWNER TO postgres;


submissions table 


-- Table: survey.submissions

-- DROP TABLE survey.submissions;

CREATE TABLE survey.submissions
(
  id character varying NOT NULL,
  username character varying,
  form_name character varying,
  has_image boolean,
  has_geopoint boolean,
  has_audio boolean,
  submission_date timestamp without time zone,
  private boolean,
  CONSTRAINT submission_pk PRIMARY KEY (id),
  CONSTRAINT username_user_fk FOREIGN KEY (username)
      REFERENCES authentication."user" (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE survey.submissions
  OWNER TO postgres;







