INSERT INTO user (username, registered)
VALUES ('kirk', current_timestamp),
  ('spock', current_timestamp),
  ('bones', current_timestamp);

INSERT INTO following (follower_id, following_id)
  SELECT
    u1.id,
    u2.id
  FROM user u1, user u2
  WHERE u1.username = 'spock'
        AND u2.username = 'kirk';

INSERT INTO following (follower_id, following_id)
  SELECT
    u1.id,
    u2.id
  FROM user u1, user u2
  WHERE u1.username = 'bones'
        AND u2.username = 'kirk';
