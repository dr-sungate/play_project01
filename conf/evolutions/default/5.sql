# create_agencies

# --- !Ups
CREATE TABLE agencies (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	agency_name varchar(255) NOT NULL,
	type       varchar(100),
	status     varchar(100) NOT NULL,
	memo       text,
	is_disabled boolean,
	timezone_id int(10),
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX status_index ON agencies(status);
CREATE INDEX is_disabled_index ON agencies(is_disabled);

# --- !Downs
DROP TABLE agencies;

