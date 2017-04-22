DROP TABLE scheduled_message IF EXISTS;
CREATE TABLE scheduled_message (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  posted_by_id INT,
  text         VARCHAR(140),
  post_at      TIMESTAMP
);
