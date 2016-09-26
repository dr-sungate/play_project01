# create_login_histories

# --- !Ups
CREATE TABLE login_histories (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	account_id int(10),
	input_login_id  text,
	user_agent     text,
	ipaddress   varchar(255),
	is_logined boolean,
	created_date	datetime NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX account_id_index ON login_histories(account_id);


# --- !Downs
DROP TABLE login_histories;

