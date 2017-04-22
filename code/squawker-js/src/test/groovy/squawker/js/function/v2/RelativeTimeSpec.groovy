package squawker.js.function.v2

import java.util.function.Consumer
import javax.script.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import static java.time.Instant.now
import static java.time.temporal.ChronoUnit.*

@SuppressWarnings("ChangeToOperator")
class RelativeTimeSpec extends Specification {

  @Shared ScriptEngine engine
  @Shared CompiledScript script

  def setupSpec() {
    def manager = new ScriptEngineManager()
    engine = manager.getEngineByName("nashorn")
    getClass().getResource("relative-time.js").withReader { reader ->
      script = ((Compilable) engine).compile(reader) // <1>
    }
    script.eval()
  }

  @Delegate Invocable invocableScriptEngine = engine as Invocable

  // tag::mock-callback[]
  @Unroll
  def "can pass result to a callback"() {
    given:
    def callback = Mock(Consumer) // <1>

    when:
    invokeFunction("relativeTime", timestamp.toEpochMilli(), callback)

    then:
    1 * callback.accept(expected) // <2>

    where:
    timestamp               | expected
    now()                   | "just now"
    now().minus(1, MINUTES) | "a minute ago"
    now().minus(5, MINUTES) | "a few minutes ago"
    now().minus(1, HOURS)   | "an hour ago"
    now().minus(2, HOURS)   | "earlier today"
    now().minus(1, DAYS)    | "yesterday"
  }
  // end::mock-callback[]

  }
