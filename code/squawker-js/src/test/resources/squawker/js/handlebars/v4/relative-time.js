function relativeTime(timestamp) {
  var now = new Date();
  var difference = now.getTime() - timestamp;
  if (difference >= 86400000) {
    return 'yesterday';
  } else if (difference >= 7200000) {
    return 'earlier today';
  } else if (difference >= 3600000) {
    return 'an hour ago';
  } else if (difference >= 120000) {
    return 'a few minutes ago';
  } else if (difference >= 60000) {
    return 'a minute ago';
  } else {
    return 'just now';
  }
}
