# create_tax_rates

# --- !Ups
CREATE TABLE tax_rates (
    id             int(10) NOT NULL AUTO_INCREMENT,
	tax_rate       decimal(10,2),
	start_from     datetime,
	created_date   datetime NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX start_from_index ON tax_rates(start_from);

# --- !Downs
DROP TABLE tax_rates;

