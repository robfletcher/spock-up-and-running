package squawker.js.handlebars.v1

import squawker.Message
import squawker.User
import squawker.js.handlebars.Specification
import static java.time.Instant.now

class MessageRenderingSpec extends Specification {

  // tag::render-message[]
  def "can render a message"() {
    given:
    def template = compile("message.hbs") // <1>

    expect:
    with(render(template, message)) { // <2>
      find(".text").text() == message.text
      find("footer a").attr("href") == "/users/$user.username"
      find("footer a").text() == user.toString()
    }

    where:
    user = new User("spock")
    message = new Message(user, "Fascinating", now())
  }
  // end::render-message[]
}
