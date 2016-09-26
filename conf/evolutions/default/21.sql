# create_timezones

# --- !Ups
CREATE TABLE timezones (
    id             int(10) NOT NULL AUTO_INCREMENT,
	timezone_value  varchar(255),
	cities        varchar(255),
	gmt       varchar(100) NOT NULL,
	created_date   datetime NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX timezone_value_index ON timezones(timezone_value);

# --- !Downs
DROP TABLE timezones;

