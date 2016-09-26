# create_contracts

# --- !Ups
CREATE TABLE contracts (
	id          bigint(20) NOT NULL AUTO_INCREMENT,
	client_id	bigint(20) NOT NULL,
	registed_date datetime,
	activated_date datetime,
	close_date  datetime,
	invoice_issue_type varchar(100),
	memo        text,
	status      varchar(100) NOT NULL,
	is_disabled boolean,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX client_id_index ON contracts(client_id);
CREATE INDEX status_index ON contracts(status);
CREATE INDEX is_disabled_index ON contracts(is_disabled);

# --- !Downs
DROP TABLE contracts;

