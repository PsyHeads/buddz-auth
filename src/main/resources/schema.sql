--------- Spring-security tables begin ---------

DROP SEQUENCE public.groups_id_seq;
CREATE SEQUENCE public.groups_id_seq
  INCREMENT 0
  MINVALUE 0
  MAXVALUE 0
  START 0
  CACHE 0;
ALTER TABLE public.groups_id_seq
  OWNER TO buddz;
  
DROP SEQUENCE public.group_members_id_seq;
CREATE SEQUENCE public.group_members_id_seq
  INCREMENT 0
  MINVALUE 0
  MAXVALUE 0
  START 0
  CACHE 0;
ALTER TABLE public.group_members_id_seq
  OWNER TO buddz;

  
DROP TABLE public.users;
CREATE TABLE public.users
(
  username character varying(50) NOT NULL,
  password character varying(68) NOT NULL,
  enabled boolean NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.users
  OWNER TO buddz;
  
-- DROP TABLE public.groups;

CREATE TABLE public.groups
(
  id integer NOT NULL DEFAULT nextval('groups_id_seq'::regclass),
  group_name character varying(128) NOT NULL,
  CONSTRAINT pk_groups PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.groups
  OWNER TO buddz;
  
-- DROP TABLE public.group_members;

CREATE TABLE public.group_members
(
  id bigint NOT NULL DEFAULT nextval('group_members_id_seq'::regclass),
  username character varying(50) NOT NULL,
  group_id integer NOT NULL,
  CONSTRAINT pk_group_authorities PRIMARY KEY (id),
  CONSTRAINT fk_group_members_group FOREIGN KEY (group_id)
      REFERENCES public.groups (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.group_members
  OWNER TO buddz;

-- DROP TABLE public.group_authorities;

CREATE TABLE public.group_authorities
(
  group_id integer,
  authority character varying(64),
  CONSTRAINT fk_group_authorities_group FOREIGN KEY (group_id)
      REFERENCES public.groups (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.group_authorities
  OWNER TO buddz;

--------- Spring-security tables end ---------
  
--------- OAuth AuthServer tables begin ---------

create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256),
  CONSTRAINT pk_oauth_client_details PRIMARY KEY (client_id)
)WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_client_details
  OWNER TO buddz;

create table oauth_client_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256)
)WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_client_token
  OWNER TO buddz;

create table oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
)WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_access_token
  OWNER TO buddz;
;

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
)WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_refresh_token
  OWNER TO buddz;
;

create table oauth_code (
  code VARCHAR(256), authentication bytea
)WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_code
  OWNER TO buddz;
;

create table oauth_approvals (
  userId VARCHAR(256),
  clientId VARCHAR(256),
  `scope` VARCHAR(256),
  status VARCHAR(10),
  expiresAt TIMESTAMP,
  lastModifiedAt TIMESTAMP
)WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_approvals
  OWNER TO buddz;
;

--------- OAuth AuthServer tables end ---------

--------- Business tables begin ---------

DROP SEQUENCE public.buddz_user_id_seq;
CREATE SEQUENCE public.buddz_user_id_seq
  INCREMENT 0
  MINVALUE 0
  MAXVALUE 0
  START 0
  CACHE 0;
ALTER TABLE public.buddz_user_id_seq
  OWNER TO buddz;

CREATE TABLE public.buddz_user
(
  id bigint NOT NULL DEFAULT nextval('buddz_user_id_seq'::regclass),
  username character varying(50) NOT NULL,
  first_name character varying(64) NOT NULL,
  last_name character varying(64),
  address1 character varying(128) NOT NULL,
  address2 character varying(128),
  state character varying(64) NOT NULL,
  city character varying(64) NOT NULL,
  country character varying(64) NOT NULL,
  country_code character varying(8) NOT NULL,
  phone_number character varying(16) NOT NULL,
  zip_code character varying(16) NOT NULL,
  CONSTRAINT pk_buddz_user PRIMARY KEY (id),
  CONSTRAINT fk_buddz_user_users FOREIGN KEY (username)
      REFERENCES public.users (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.buddz_user
  OWNER TO buddz;

--------- Business tables end ---------
