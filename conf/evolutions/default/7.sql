# create_billing_destinations

# --- !Ups
CREATE TABLE  billing_destinations (
	id             bigint(20) NOT NULL AUTO_INCREMENT,
	agency_id      bigint(20)  NOT NULL,
	billing_destination_name   varchar(255),
	invoice_issue_type varchar(100),
	due_date_month varchar(100),
	due_date_day   int(10),
	closing_date   int(10),
	pca_code       varchar(255),
	pca_common_name varchar(255),
	memo            text,
	updater         int(10),
	created_date    datetime NOT NULL,
	updated_date    timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX agency_id_index ON billing_destinations(agency_id);

# --- !Downs
DROP TABLE billing_destinations;

