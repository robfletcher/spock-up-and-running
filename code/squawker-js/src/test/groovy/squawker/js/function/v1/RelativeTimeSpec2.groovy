package squawker.js.function.v1

import javax.script.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import static java.time.Instant.now
import static java.time.temporal.ChronoUnit.*

@SuppressWarnings("ChangeToOperator")
class RelativeTimeSpec2 extends Specification {

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

  // tag::invocable-delegate[]
  @Delegate Invocable invocableScriptEngine = engine as Invocable

  @Unroll
  def "can render relative timestamp"() {
    expect:
    invokeFunction("relativeTime", timestamp.toEpochMilli()) == expected

    where:
    timestamp               | expected
    now()                   | "just now"
    now().minus(1, MINUTES) | "a minute ago"
    now().minus(5, MINUTES) | "a few minutes ago"
    now().minus(1, HOURS)   | "an hour ago"
    now().minus(2, HOURS)   | "earlier today"
    now().minus(1, DAYS)    | "yesterday"
  }
  // end::invocable-delegate[]
}
