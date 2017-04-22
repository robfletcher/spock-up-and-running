package squawker.js.handlebars.v2

import squawker.Message
import squawker.User
import squawker.js.handlebars.Specification
import static java.time.Instant.now

class MessageRenderingSpec extends Specification {

  // tag::convert-to-map[]
  def "can render a message"() {
    given:
    def template = compile("message.hbs")

    and:
    def messageObj = [
      text    : message.text,
      postedBy: message.postedBy,
      postedAt: message.postedAt.toEpochMilli() // <1>
    ]

    expect:
    with(render(template, messageObj)) {
      find(".text").text() == message.text
      find("footer a").attr("href") == "/users/$user.username"
      find("footer a").text() == user.toString()
      find("footer time").text() == message.postedAt.toEpochMilli() as String
    }

    where:
    user = new User("spock")
    message = new Message(user, "Fascinating", now())
  }
  // end::convert-to-map[]
}
