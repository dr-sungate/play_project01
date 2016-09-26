# create_invoices

# --- !Ups
CREATE TABLE invoices (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	invoicec_view_id varchar(255) NOT NULL,
	application_id bigint(20) NOT NULL,
	agency_id  bigint(20) NOT NULL,
	billing_destination_id  bigint(20) NOT NULL,
	invoicec_to     varchar(255),
	invoicec_subject varchar(255),
	invoicec_total DECIMAL(30,2),
	invoicec_taxed_total DECIMAL(30,2),
	invoicec_taxrate DECIMAL(10,2),
	pca_code        varchar(255),
	pca_common_name varchar(255),
	memo            text,
	status          varchar(100) NOT NULL,
	cancel_status   varchar(100),
	is_disabled     boolean,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX invoicec_view_id_index ON invoices(invoicec_view_id);
CREATE INDEX application_id_index ON invoices(application_id);
CREATE INDEX agency_id_index ON invoices(agency_id);
CREATE INDEX billing_destination_id_index ON invoices(billing_destination_id);
CREATE INDEX status_index ON invoices(status);
CREATE INDEX cancel_status_index ON invoices(cancel_status);
CREATE INDEX is_disabled_index ON invoices(is_disabled);


# --- !Downs
DROP TABLE invoices;

