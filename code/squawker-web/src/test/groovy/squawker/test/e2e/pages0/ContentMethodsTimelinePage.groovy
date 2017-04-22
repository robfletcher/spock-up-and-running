package squawker.test.e2e.pages0

import geb.Page

class ContentMethodsTimelinePage extends Page {

  static url = "#/timeline"

  static atCheckWaiting = true
  static at = {
    $(".page-header").text() == "Timeline"
  }

  static content = {}

  // tag::content-properties[]
  String getMessageText() {
    $(".sq-message-text").text()
  }

  String getPostedBy() {
    $(".sq-posted-by").text()
  }
  // end::content-properties[]
}
