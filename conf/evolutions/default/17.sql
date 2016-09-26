# create_auth_code

# --- !Ups
CREATE TABLE auth_codes (
    authorization_code varchar(255) NOT NULL ,
    oauth_user_guid          binary(16) NOT NULL,
    oauth_client_id    binary(16) NOT NULL,
    redirect_uri       varchar(255),
    scope              varchar(255),
    expires_in         int NOT NULL,
    created_date       datetime NOT NULL,
    PRIMARY KEY (authorization_code)
) ENGINE=InnoDB;

CREATE INDEX oauth_user_guid_index ON auth_codes(oauth_user_guid);
CREATE INDEX oauth_client_id_index ON auth_codes(oauth_client_id);

# --- !Downs
DROP TABLE auth_code;
