# create_applications

# --- !Ups
CREATE TABLE applications (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	app_name   varchar(255) NOT NULL,
	url        varchar(255),
	server_ips text,
	status     varchar(100) NOT NULL,
	is_disabled boolean,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX status_index ON applications(status);
CREATE INDEX is_disabled_index ON applications(is_disabled);

# --- !Downs
DROP TABLE applications;

