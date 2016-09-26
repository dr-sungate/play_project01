# create_access_token

# --- !Ups
CREATE TABLE access_tokens (
    access_token    varchar(255) NOT NULL ,
    refresh_token   varchar(255),
    oauth_user_guid       binary(16) NOT NULL,
    oauth_client_id binary(16) NOT NULL,
    redirect_uri    varchar(255),
    scope           varchar(255),
    expires_in      int NOT NULL,
    created_date    datetime NOT NULL,
    PRIMARY KEY (access_token)
) ENGINE=InnoDB;

CREATE INDEX refresh_token_id_index ON access_tokens(refresh_token);
CREATE INDEX oauth_user_guid_index ON access_tokens(oauth_user_guid);
CREATE INDEX oauth_client_id_index ON access_tokens(oauth_client_id);

# --- !Downs
DROP TABLE access_token;

