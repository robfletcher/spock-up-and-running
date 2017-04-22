package squawker.js

import java.util.function.Function
import javax.script.Compilable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import groovy.transform.Memoized
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TemplateRenderingSpec extends Specification {

  @Shared ScriptEngine engine

  def setupSpec() {
    def manager = new ScriptEngineManager()
    engine = manager.getEngineByName("nashorn")
    getClass().getResource("/handlebars.js").withReader { reader ->
      def handlebars = (engine as Compilable).compile(reader)
      handlebars.eval()
    }
  }

  def "can render a simple template"() {
    given:
    def template = engine.eval """
      Handlebars.compile('<div>{{message}}</div>');
    """

    expect:
    template(null, [message: "o hai"]) == "<div>o hai</div>"
  }

  @Unroll
  def "can render a #value conditional"() {
    given:
    def template = engine.eval """
      Handlebars.compile('<div>{{#if value}}Yes{{else}}No{{/if}}</div>');
    """

    expect:
    template(null, [value: value]) == expected

    where:
    value | expected
    true  | "<div>Yes</div>"
    false | "<div>No</div>"
  }

  def "can render an iterable"() {
    given:
    def template = engine.eval """
      Handlebars.compile('<ul>{{#each items}}<li>{{this}}</li>{{/each}}</ul>');
    """

    expect:
    template(null, [items: array("a", "b", "c")]) == "<ul><li>a</li><li>b</li><li>c</li></ul>"
  }

  def "can use a mock"() {
    given:
    def template = engine.eval """
      function(message, callback) {
        callback(message.toUpperCase());
      }
    """

    and:
    def mock = Mock(Function)

    when:
    template(null, "o hai", mock)

    then:
    1 * mock.apply("O HAI")
  }

  private Object array(Object... elements) {
    from()(null, elements.toList())
  }

  @Memoized
  private Object from() {
    engine.eval("Java.from")
  }
}
