function relativeTime(timestamp, callback) {
  var now = new Date();
  var difference = now.getTime() - timestamp;
  var result;
  if (difference >= 86400000) {
    result = 'yesterday';
  } else if (difference >= 7200000) {
    result = 'earlier today';
  } else if (difference >= 3600000) {
    result = 'an hour ago';
  } else if (difference >= 120000) {
    result = 'a few minutes ago';
  } else if (difference >= 60000) {
    result = 'a minute ago';
  } else {
    result = 'just now';
  }

  if (callback === undefined) {
    return result;
  } else {
    callback(result);
  }
}
