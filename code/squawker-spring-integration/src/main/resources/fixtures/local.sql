-- create 2 users
INSERT INTO user (username, registered)
VALUES ('kirk', current_timestamp), ('spock', current_timestamp);

-- give them API keys
INSERT INTO api_token (token, user_id)
  SELECT
    random_uuid(),
    id
  FROM user
  WHERE username = 'kirk';

INSERT INTO api_token (token, user_id)
  SELECT
    random_uuid(),
    id
  FROM user
  WHERE username = 'spock';

-- have each post a message
INSERT INTO message (posted_by_id, `text`, posted_at)
  SELECT
    id,
    'Report, Mr… @spock',
    current_timestamp
  FROM user
  WHERE username = 'kirk';

INSERT INTO message (posted_by_id, `text`, posted_at)
  SELECT
    id,
    'Fascinating!',
    current_timestamp
  FROM user
  WHERE username = 'spock';

-- tie up mentions
INSERT INTO mention (user_id, message_id)
  SELECT
    u.id,
    m.id
  FROM user u, message m
  WHERE u.username = 'spock'
        AND m.text = 'Report, Mr… @spock';

-- one user follows the other
INSERT INTO following (follower_id, following_id)
  SELECT
    u1.id,
    u2.id
  FROM user u1, user u2
  WHERE u1.username = 'spock'
        AND u2.username = 'kirk';
