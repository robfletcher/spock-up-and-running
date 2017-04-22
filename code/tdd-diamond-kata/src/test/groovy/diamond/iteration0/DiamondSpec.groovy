/*
 * Copyright 2015 the original author or authors.
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

package diamond.iteration0

import java.util.function.Function
import spock.genesis.Gen
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class DiamondSpec extends Specification {

  @Subject Function<Character, List<String>> diamond

  def setup() {
    diamond = createDiamond()
  }

  Function<Character, List<String>> createDiamond() {
    new Diamond()
  }

  // tag::pathological-case[]
  @Shared char aChar = 'A'
  @Shared char zChar = 'Z'
  @Shared Range<Character> validRange = aChar..zChar

  @Unroll("rejects '#c'")
  def "rejects characters outside the range A-Z"() {
    when:
    diamond.apply(c)

    then:
    thrown IllegalArgumentException

    where:
    c << Gen.character
            .filter { !validRange.contains(it) }
            .take(50) // <1>
  }
  // end::pathological-case[]
}
