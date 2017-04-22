package primer
// tag::class-with-properties[]
class Person {
  String firstName
  String lastName
// end::class-with-properties[]

  // tag::defined-getter[]
  String middleName

  Optional<String> getMiddleName() {
    Optional.ofNullable(middleName)
  }
  // end::defined-getter[]

  String getName() {
    firstName + " " + lastName
  }

  @Override
  String toString() {
    name
  }
// tag::class-with-properties[]
}
// end::class-with-properties[]
