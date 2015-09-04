CREATE DATABASE bugzilla character set  utf8 collate utf8_general_ci;

USE bugzilla;

CREATE TABLE user (
	user_id int AUTO_INCREMENT,
	name varchar(50) NOT NULL,
	password varchar(50) NOT NULL,
	email varchar(50) NOT NULL,
	role varchar(50),
	register_time datetime NOT NULL,
	CONSTRAINT pk_user_id PRIMARY KEY (user_id),
	CONSTRAINT uc_user_name UNIQUE (name),
	CONSTRAINT uc_user_email UNIQUE (email)
);



CREATE TABLE project (
	project_id int AUTO_INCREMENT,
	creator int NOT NULL,
	leader int NOT NULL,
	name varchar(50) NOT NULL,
	description text NOT NULL,
	project_status varchar(50) NOT NULL,
	sprint varchar(50) NOT NULL,
	create_date datetime NOT NULL,
	CONSTRAINT pk_project_id PRIMARY KEY (project_id),
	CONSTRAINT fk_project_creator FOREIGN KEY (creator) REFERENCES user(user_id),
	CONSTRAINT fk_project_leader FOREIGN KEY (leader) REFERENCES user(user_id)
);

CREATE TABLE member (
	user_id int,
	project_id int,
	join_time datetime NOT NULL,
	CONSTRAINT pk_member PRIMARY KEY (user_id, project_id),
	CONSTRAINT fk_member_user_id FOREIGN KEY (user_id) REFERENCES user(user_id),
	CONSTRAINT fk_member_pro_id FOREIGN KEY (project_id) REFERENCES project(project_id)
);

CREATE TABLE bug (
	bug_id int AUTO_INCREMENT, 
	creator int NOT NULL,
	project_id int NOT NULL,
	name varchar(50) NOT NULL,
	description text NOT NULL,
	bug_status varchar(50) NOT NULL,
	sprint varchar(50) NOT NULL,
	tag varchar(50),
	severity tinyint NOT NULL,
	create_date datetime NOT NULL,
	CONSTRAINT pk_bug_id PRIMARY KEY (bug_id),
	CONSTRAINT fk_bug_creator FOREIGN KEY (creator) REFERENCES user(user_id),
	CONSTRAINT fk_bug_project_id FOREIGN KEY (project_id) REFERENCES project(project_id)
);

CREATE TABLE assignment (
	assign_id int AUTO_INCREMENT,
	bug_id int NOT NULL,
	operate_user_id int NOT NULL,
	assigned_user_id int NOT NULL,
	status varchar(50) NOT NULL,
	begin_time datetime NOT NULL,
	end_time datetime,
	CONSTRAINT pk_assign_id PRIMARY KEY (assign_id),
	CONSTRAINT fk_assign_bug_id FOREIGN KEY (bug_id) REFERENCES bug(bug_id),
	CONSTRAINT fk_assign_operate_id FOREIGN KEY (operate_user_id) REFERENCES user(user_id),
	CONSTRAINT fk_assign_assigned_id FOREIGN KEY (assigned_user_id) REFERENCES user(user_id)
);

CREATE TABLE comment (
	comment_id int AUTO_INCREMENT,
	user_id int NOT NULL,
	bug_id int NOT NULL,
	refer_id int,
	content text NOT NULL,
	pub_time datetime NOT NULL,
	CONSTRAINT pk_comment_id PRIMARY KEY (comment_id),
	CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES user(user_id),
	CONSTRAINT fk_comment_bug_id FOREIGN KEY (bug_id) REFERENCES bug(bug_id)
);

CREATE TABLE file (
	file_id int AUTO_INCREMENT,
	bug_id int NOT NULL,
	save_path varchar(255) NOT NULL,
	is_image bit NOT NULL DEFAULT 1,
	CONSTRAINT pk_image_id PRIMARY KEY (file_id),
	CONSTRAINT fk_bug_id FOREIGN KEY (bug_id) REFERENCES bug(bug_id)
);

