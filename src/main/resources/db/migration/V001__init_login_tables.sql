CREATE TABLE login
(
  id       serial              NOT NULL,
  email    varchar(255)        NOT NULL,
  username varchar(255) UNIQUE NOT NULL,
  password varchar(255)        NOT NULL,
  deleted  boolean DEFAULT FALSE,
  PRIMARY KEY (id)
);

comment on table login
  is 'Users/Logins that are able to login into the webapp';

create index login_email_index
  on login (email);

CREATE VIEW v_active_logins AS
SELECT *
FROM login l
WHERE l.deleted = false;

comment on view v_active_logins
  is 'Active Logins, that are able to log into the app';

-- Default Data


