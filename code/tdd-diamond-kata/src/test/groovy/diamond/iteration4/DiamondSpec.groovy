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

package diamond.iteration4

import java.util.function.Function
import spock.lang.Unroll

class DiamondSpec extends diamond.iteration3.DiamondSpec {

  @Override
  Function<Character, List<String>> createDiamond() {
    new Diamond()
  }

  // tag::symmetry[]
  @Unroll
  def "diamond(#c) is symmetrical"() {
    given:
    def result = diamond.apply(c)

    expect:
    result == result.reverse() // <1>

    and:
    result.every {
      it == it.reverse() // <2>
    }

    where:
    c << testRange
  }
  // end::symmetry[]

  // tag::quadrant[]
  @Unroll
  def "the appropriate character appears in each row in the correct columns in diamond(#c)"() {
    given:
    def result = diamond.apply(c)

    and:
    int midpoint = result.size().intdiv(2)

    expect:
    for (rowChar in aChar..c) {
      int y = (rowChar - aChar)
      int x = midpoint - y
      assert result[y].charAt(x) == rowChar
    }

    where:
    c << testRange
  }
  // end::quadrant[]
}
