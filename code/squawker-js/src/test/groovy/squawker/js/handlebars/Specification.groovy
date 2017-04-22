package squawker.js.handlebars

import javax.script.Compilable
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import jdk.nashorn.api.scripting.JSObject
import jodd.jerry.Jerry
import spock.lang.Shared
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS
import static jodd.jerry.Jerry.jerry as $

abstract class Specification extends spock.lang.Specification {

  // tag::compile-templates[]
  @Shared ScriptEngine engine
  @Shared JSObject handlebars

  def setupSpec() {
    def manager = new ScriptEngineManager()
    engine = manager.getEngineByName("nashorn") // <1>
    loadResource("/handlebars.js") { reader ->
      (engine as Compilable).compile(reader).eval() // <2>
    }
    handlebars = engine.eval("Handlebars") as JSObject // <3>
  }

  @Delegate Invocable invocableEngine
  // end::compile-templates[]
  // tag::jackson[]
  def mapper = new ObjectMapper()
  // tag::compile-templates[]

  def setup() {
    // end::jackson[]
    invocableEngine = engine as Invocable
    // end::compile-templates[]

    // tag::jackson[]
    mapper.registerModule(new JavaTimeModule()) // <1>
    mapper.disable(WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS) // <2>
    // tag::compile-templates[]
  }
  // end::jackson[]

  protected JSObject compile(String path) {
    loadResource(path) { reader ->
      invokeMethod(handlebars, "compile", reader.text) as JSObject // <4>
    }
  }

  // end::compile-templates[]
  // tag::register-partial[]
  protected void registerPartial(String name, String path) {
    loadResource(path) { reader ->
      invokeMethod(handlebars, "registerPartial", name, reader.text)
    }
  }
  // end::register-partial[]

  // tag::compile-templates[]
  protected <T> T loadResource(
    String path,
    @ClosureParams(value = SimpleType, options = "java.io.Reader")
      Closure<T> callback) {
    getClass().getResource(path).withReader(callback) //<5>
  }
  // end::compile-templates[]

  // tag::render-helper[]
  Jerry render(JSObject template, Object... parameters) {
    $(template(null, *parameters) as String)
  }
  // end::render-helper[]
}
