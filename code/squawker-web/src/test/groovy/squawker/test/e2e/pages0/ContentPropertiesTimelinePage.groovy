package squawker.test.e2e.pages0

import geb.Page

class ContentPropertiesTimelinePage extends Page {

  static url = "#/timeline"

  static atCheckWaiting = true
  static at = {
    $(".page-header").text() == "Timeline"
  }

  static content = {}

  // tag::content-properties[]
  String messageText = $(".sq-message-text").text()

  String postedBy = $(".sq-posted-by").text()
  // end::content-properties[]
}
