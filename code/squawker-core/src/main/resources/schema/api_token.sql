CREATE TABLE IF NOT EXISTS api_token (
  token   VARCHAR(36) UNIQUE,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES user (id)
);
