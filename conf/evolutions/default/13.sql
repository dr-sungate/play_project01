# create_clients

# --- !Ups
CREATE TABLE clients (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	application_id bigint(20) NOT NULL,
	agency_id  bigint(20) NOT NULL,
	app_client_id  varchar(255) NOT NULL,
	name       varchar(255),
	email      varchar(255),
	role       varchar(255),
	app_created_date datetime,
	last_login_date datetime,
	last_login_ipaddress varchar(255),
	login_count int(10),
	memo       text,
	status varchar(100) NOT NULL,
	is_disabled boolean,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;


CREATE INDEX application_id_index ON clients(application_id);
CREATE INDEX agency_id_index ON clients(agency_id);
CREATE INDEX app_client_id_index ON clients(app_client_id);
CREATE INDEX status_index ON clients(status);
CREATE INDEX is_disabled_index ON clients(is_disabled);

# --- !Downs
DROP TABLE clients;

