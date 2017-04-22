CREATE TABLE IF NOT EXISTS message (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  posted_by_id INT,
  text         VARCHAR(140),
  posted_at    TIMESTAMP,
  FOREIGN KEY (posted_by_id) REFERENCES user (id)
);

