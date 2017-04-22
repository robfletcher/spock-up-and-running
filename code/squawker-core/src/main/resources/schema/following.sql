CREATE TABLE IF NOT EXISTS following (
  follower_id  BIGINT,
  following_id BIGINT,
  UNIQUE (follower_id, following_id),
  FOREIGN KEY (follower_id) REFERENCES user (id),
  FOREIGN KEY (following_id) REFERENCES user (id)
);
