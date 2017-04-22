DROP TABLE mention IF EXISTS;
CREATE TABLE mention (
  user_id    BIGINT,
  message_id BIGINT,
  UNIQUE (user_id, message_id),
  FOREIGN KEY (user_id) REFERENCES user (id),
  FOREIGN KEY (message_id) REFERENCES message (id)
);
