package squawker.conditional

import groovy.transform.Memoized
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification
import static ConditionalSpec.available

class ConditionalSpec extends Specification {

  // tag::memoized-function[]
  @Memoized
  static boolean available(String url) {
    try {
      url.toURL().openConnection().with {
        connectTimeout = 1000
        connect()
      }
      true
    } catch (IOException e) {
      false
    }
  }
  // end::memoized-function[]

  // tag::require-internet[]
  @Requires({
    available("http://spockframework.org/")
  })
  // end::require-internet[]
  def "a test that requires an internet connection"() {
    expect:
    1 == 1
  }

  // tag::ignore-if-env[]
  @IgnoreIf({
    env.SKIP_INTEGRATION_TESTS == "yes"
  })
  // end::ignore-if-env[]
  def "a slow test"() {
    expect:
    1 == 1
  }

}
