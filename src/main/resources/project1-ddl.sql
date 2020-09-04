

--FIRST TABLE 
--USER roles 

CREATE TABLE ers_user_roles (

role_id serial, 
role_name varchar(10)UNIQUE ,

CONSTRAINT ers_user_roles_pk 
PRIMARY KEY (role_id)
);


--ers reimbursement statuses
CREATE TABLE ers_reimbursement_statuses (
reimb_status_id serial, 
reimb_status varchar(10)UNIQUE ,

CONSTRAINT reimb_status_pk 
PRIMARY KEY (reimb_status_id)


);


--ers reimbursement types

CREATE TABLE ers_reimbursement_types (
reimb_type_id serial, 
reimb_type varchar(10)UNIQUE ,

CONSTRAINT reimb_type_pk 
PRIMARY KEY (reimb_type_id)

);


--ers users
CREATE TABLE ers_users(
ers_user_id	serial,
username varchar(25) UNIQUE,
PASSWORD varchar(256),
first_name	varchar(25),
last_name varchar(25),
email varchar(256) UNIQUE,
user_role_id int,
	
CONSTRAINT ers_users_pk
PRIMARY KEY (ers_user_id),
	
CONSTRAINT ers_user_roles_fk
FOREIGN KEY (user_role_id)
REFERENCES ers_user_roles
);

--ers reimbursements 
--change to blob or link
CREATE TABLE ers_reimbursements(
reimb_id serial ,
amount numeric(6,2) ,
submitted timestamp ,
resolved timestamp ,
description TEXT, 
receipt text, 
author_id int ,
resolver_id int , 
reimb_status_id int ,   
reimb_type_id int , 

CONSTRAINT ers_reimbursements_pk
	PRIMARY KEY (reimb_id),
	
	CONSTRAINT author_id_fk
	FOREIGN KEY (author_id)
	REFERENCES ers_users ,
	
	CONSTRAINT resolver_id_fk
	FOREIGN KEY (resolver_id)
	REFERENCES ers_users,
	
	constraint reimb_status_id_fk
	foreign key (reimb_status_id)
	references ers_reimbursement_statuses,
	
	constraint reimb_type_id_fk
	foreign key (reimb_type_id)
	references ers_reimbursement_types
);

--roles 
INSERT INTO ers_user_roles (role_name)
VALUES ('Admin' ), ('FinanceMgr') , ('Employee');

SELECT * 
FROM ers_user_roles eur ;

--users 
INSERT INTO ers_users (username, password, first_name, last_name, email, user_role_id)
values
	('aanderson', 'password', 'Alice', 'Anderson', 'aanderson@com', 1),
	('dame', 'password', 'Damian', 'Lillard', 'damedolla@com', 2),
	('rbrook', 'password', 'Russel', 'Westbrook', 'rbrook@com', 3),
	('mamba', 'password', 'Kobe', 'Bryant', 'kobe@com', 1);

SELECT * 
FROM ers_users eu ;


--ers reimbursemnts 
insert into ers_reimbursement_types (reimb_type)
values ('Food'), ('Travel'), ('Hotel'), ('Other');

insert into ers_reimbursement_statuses (reimb_status)
values ('Pending'), ('Approved'), ('Denied');

INSERT INTO ers_reimbursements (amount , description , receipt , author_id , resolver_id  , reimb_status_id , reimb_type_id )
VALUES 
(25 , 'Uber' , 'recipt for 25' , 1 , 2 , 1 , 2) ,
(15 , 'Subway' , 'recipt for 15' , 1 , 2 , 1 , 1) ,
(125 , 'Hotel Artemis', 'recipt for 125' , 1 , 2 , 1 , 3) ,
(200 , 'Java Cert', 'recipt for 200' , 1 , 2 , 1 , 4) ;

SELECT * 
FROM ers_reimbursements er ;
