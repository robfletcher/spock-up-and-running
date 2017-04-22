import org.junit.*
import squawker.*

/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class SpreadOperatorExample {

  @Test
  void spreadOperatorForProperty() {
    // tag::spread-operator-property[]
    def kirk = new User("kirk")
    def spock = new User("spock")
    def scotty = new User("scotty")
    def users = [kirk, spock, scotty]
    assert users*.username == ["kirk", "spock", "scotty"]
    // end::spread-operator-property[]
  }

  @Test
  void gpathForProperty() {
    // tag::gpath-property[]
    def kirk = new User("kirk")
    def spock = new User("spock")
    def scotty = new User("scotty")
    def users = [kirk, spock, scotty]
    assert users.username == ["kirk", "spock", "scotty"]
    // end::gpath-property[]
  }

  @Test
  void spreadOperatorForMethod() {
    def kirk = new User("kirk")
    def spock = new User("spock")
    def scotty = new User("scotty")
    def users = [kirk, spock, scotty]
    // tag::spread-operator-method[]
    assert users*.toString() == ["@kirk", "@spock", "@scotty"]
    // end::spread-operator-method[]
  }
}
