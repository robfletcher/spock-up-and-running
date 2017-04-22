package squawker.js

import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import jdk.nashorn.api.scripting.JSObject
import spock.lang.Shared
import spock.lang.Specification

@SuppressWarnings("ChangeToOperator")
class BasicScriptSpec extends Specification {

  // tag::script-engine[]
  @Shared ScriptEngine engine

  def setupSpec() {
    def manager = new ScriptEngineManager()
    engine = manager.getEngineByName("nashorn")
  }
  // end::script-engine[]

  // tag::named-function[]
  def "can call a named function"() {
    given:
    engine.eval """ // <1>
      function up(s) {
        return s.toUpperCase();
      }
    """

    expect:
    (engine as Invocable).invokeFunction("up", "Fascinating") == "FASCINATING" // <2>
  }
  // end::named-function[]

  // tag::call-function[]
  def "can call a function"() {
    given:
    def fn = engine.eval("""
      function up(s) {
        return s.toUpperCase();
      }
    """) as JSObject

    expect:
    fn.call(null, "Fascinating") == "FASCINATING"
  }
  // end::call-function[]

  def "can call a function like a Groovy closure"() {
    given:
    def fn = engine.eval("""
      function up(s) {
        return s.toUpperCase();
      }
    """) as JSObject

    // tag::groovy-style-call[]
    expect:
    fn(null, "Fascinating") == "FASCINATING"
    // end::groovy-style-call[]
  }

  // tag::bind-this[]
  def "can bind tho 'this'"() {
    given:
    def fn = engine.eval("""
      function up() {
        return this.toUpperCase(); // <1>
      }
    """) as JSObject

    expect:
    fn("Fascinating") == "FASCINATING" // <2>
  }
  // end::bind-this[]

  def "this bound in JS"() {
    given:
    engine.eval("""
      function bindAndCall() {
        // tag::bind-this-in-js[]
        var up = function() {
          return this.toUpperCase();
        }
        var boundUp = up.bind('Fascinating');
        return boundUp();
        // end::bind-this-in-js[]
      }
    """) as JSObject

    expect:
    (engine as Invocable).invokeFunction("bindAndCall") == "FASCINATING"
  }
}
