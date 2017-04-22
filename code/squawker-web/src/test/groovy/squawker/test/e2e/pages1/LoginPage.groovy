package squawker.test.e2e.pages1

import geb.Page

// tag::login-page[]
class LoginPage extends Page {

  static url = "#/login"

  static atCheckWaiting = true
  static at = {
    $(".page-header").text() == "Log in"
  }

  static content = {
    usernameField {
      $(".sq-login #username")
    }
    passwordField {
      $(".sq-login #password")
    }
    submitButton {
      $(".sq-login [type=submit]")
    }
    authenticatedUser {
      $(".sq-authenticated-user").text()
    }
  }
// end::login-page[]

  // tag::login-method[]
  void login(String username, String password) {
    usernameField.value(username)
    passwordField.value(password)
    submitButton.click()
    waitFor {
      authenticatedUser == "Logged in as @$username"
    }
  }
  // end::login-method[]
}
