package squawker.js.handlebars.v4

import javax.script.Compilable
import com.fasterxml.jackson.core.type.TypeReference
import jdk.nashorn.api.scripting.JSObject
import spock.lang.Shared
import squawker.Message
import squawker.User
import squawker.js.handlebars.Specification
import static java.time.Instant.now
import static java.time.temporal.ChronoUnit.MINUTES

class MessageRenderingSpec extends Specification {

  // tag::java-from[]
  @Shared JSObject java

  // tag::register-helper[]
  def setupSpec() {

    // ...

    // end::java-from[]
    loadResource("relative-time.js") { reader ->
      (engine as Compilable).compile(reader).eval()
    }
    engine.eval """
      Handlebars.registerHelper("relativeTime", relativeTime);
    """
    // end::register-helper[]

    // tag::java-from[]
    java = engine.eval("Java") as JSObject
    // tag::register-helper[]
  }
  // end::register-helper[]
  // end::java-from[]

  // tag::expect-relative-time[]
  def "can render a message"() {
    given:
    def template = compile("message.hbs")

    and:
    def messageObj = mapper.convertValue(message, Map)

    expect:
    with(render(template, messageObj)) {
      find(".text").text() == message.text
      find("footer a").attr("href") == "/users/$user.username"
      find("footer a").text() == user.toString()
      find("footer time").text() == "just now" // <1>
    }

    where:
    user = new User("spock")
    message = new Message(user, "Fascinating", now())
  }
  // end::expect-relative-time[]

  // tag::render-list-of-messages[]
  def "can render a list of messages"() {
    given:
    registerPartial("message", "message.hbs") // <1>
    def template = compile("messages.hbs") // <2>

    and:
    def messagesArray = array(messages) // <3>

    expect:
    with(render(template, messagesArray)) { // <4>
      def elements = find(".message-list article")
      elements.size() == messages.size()
      elements.first().find(".text").text() == messages[0].text
      elements.first().find("time").text() == "just now"
      elements.last().find(".text").text() == messages[1].text
      elements.last().find("time").text() == "a few minutes ago"
    }

    where:
    user = new User("spock")
    messages = [
      new Message(user, "Fascinating", now()),
      new Message(user, "Live long and prosper", now().minus(10, MINUTES))
    ]
  }
  // end::render-list-of-messages[]

  // tag::convert-array-and-elements[]
  Object array(List<?> list) {
    def targetType = new TypeReference<List<Map>>() {} // <1>
    def json = mapper.convertValue(list, targetType) // <2>
    invokeMethod(java, "from", json) // <3>
  }
  // end::convert-array-and-elements[]

  // tag::convert-to-array[]
  Object array(Object... elements) {
    invokeMethod(java, "from", elements.toList())
  }
  // end::convert-to-array[]
}
