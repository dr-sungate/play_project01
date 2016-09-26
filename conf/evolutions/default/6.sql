# create_application_agency

# --- !Ups
CREATE TABLE application_agency (
	application_id    bigint(20)  NOT NULL,
	agency_id bigint(20) NOT NULL,
	updater    int(10),
	created_date datetime NOT NULL,
	PRIMARY KEY (application_id, agency_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE application_agency;

