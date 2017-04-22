INSERT INTO mention (user_id, message_id)
  SELECT
    u.id,
    :message.id
  FROM user u
  WHERE u.username = :username AND NOT EXISTS(
    SELECT 1
    FROM mention
    WHERE user_id = u.id AND message_id = :message.id
  )
