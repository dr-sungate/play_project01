# create_accounts

# --- !Ups
CREATE TABLE accounts (
	id         int(10) NOT NULL AUTO_INCREMENT,
	login_id    varchar(255) NOT NULL,
	password   varchar(255) NOT NULL,
	name       varchar(255) NOT NULL,
	email      varchar(255),
	role       varchar(100) NOT NULL,
	is_disabled boolean,
	updater    int(10),
	created_date	datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX login_id_index ON accounts(login_id);
CREATE INDEX is_disabled_index ON accounts(is_disabled);

 
# --- !Downs
DROP TABLE accounts;

