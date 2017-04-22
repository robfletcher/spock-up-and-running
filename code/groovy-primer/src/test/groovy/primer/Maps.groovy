package primer

import org.junit.Test

class Maps {

  @Test
  void mapLiteral() {
    // tag::map-literal[]
    def crew = [captain: "Kirk", science: "Spock", medical: "Bones"]
    // end::map-literal[]

    assert crew instanceof LinkedHashMap
    assert crew.size() == 3
    assert crew.keySet().every { it instanceof String }

    // tag::order-guarantee[]
    assert crew.values().asList() == ["Kirk", "Spock", "Bones"]
    // end::order-guarantee[]
  }

  @Test
  void quotedKeys() {
    // tag::quoted-keys[]
    def crew = ["captain": "Kirk", "science": "Spock", "medical": "Bones"]
    // end::quoted-keys[]

    assert crew instanceof LinkedHashMap
    assert crew.size() == 3
    assert crew.keySet().every { it instanceof String }
  }

  @Test
  void programmaticKeys() {
    // tag::programmatic-keys[]
    def posts = ["captain", "science officer", "chief medical officer"]
    def crew = [(posts[0]): "Kirk", (posts[1]): "Spock", (posts[2]): "Bones"]
    // end::programmatic-keys[]

    assert crew.size() == 3
    assert crew.keySet().every { it instanceof String }
  }

  @Test
  void indexing() {
    def crew = [captain: "Kirk", science: "Spock", medical: "Bones"]

    // tag::map-indexes[]
    def post = "captain"
    assert crew[post] == "Kirk" // <1>
    assert crew.captain == "Kirk" // <2>

    crew.medical = "McCoy" // <3>
    assert crew == [captain: "Kirk", science: "Spock", medical: "McCoy"]

    crew["engineer"] = "Scotty" // <4>
    assert crew.size() == 4
    assert crew.engineer == "Scotty"
    // end::map-indexes[]
  }

  @Test
  void mapConstructor() {
    // tag::map-constructor[]
    def person = new Person(firstName: "James", lastName: "Kirk")
    assert person.firstName == "James"
    assert person.lastName == "Kirk"
    // end::map-constructor[]
  }
}
