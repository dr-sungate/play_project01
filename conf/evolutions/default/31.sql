# create_invoice_processdates

# --- !Ups
CREATE TABLE invoice_processdates (
	invoice_id     bigint(20) NOT NULL,
	issue_date      date,
	apply_date      date,
	payed_date      date,
	sent_date       date,
	cancel_date     date,
	payment_due_date date,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (invoice_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE invoice_processdates;

