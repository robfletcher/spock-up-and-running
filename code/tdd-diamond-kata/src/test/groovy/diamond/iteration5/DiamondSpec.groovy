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

package diamond.iteration5

import java.util.function.Function
import spock.lang.Unroll

class DiamondSpec extends diamond.iteration4.DiamondSpec {

  @Override
  Function<Character, List<String>> createDiamond() {
    new Diamond()
  }

  // tag::flatmap-that-shit[]
  @Unroll
  def "#rowChar appears in the correct row and column in diamond(#c)"() {
    given:
    def result = diamond.apply(c)

    and:
    int midpoint = result.size().intdiv(2)
    int y = (rowChar - aChar)
    int x = midpoint - y

    expect:
    result[y].charAt(x) == rowChar

    where:
    row << testRange.collectMany { c2 -> // <1>
      (aChar..c2).collect { new Tuple(c2, it) } // <2>
    }
    c = row[0] // <3>
    rowChar = row[1]
  }
  // end::flatmap-that-shit[]

  // tag::overspecified-padding[]
  @Unroll
  def "areas around the character `#rowChar` in diamond(#c) are filled with padding"() {
    given:
    def result = diamond.apply(c)

    and:
    int midpoint = result.size().intdiv(2)
    int y = (rowChar - aChar)
    int x = midpoint - y

    expect:
    new StringBuilder(result[y][0..midpoint])
      .deleteCharAt(x)
      .toString()
      .every { it == '-' }

    where:
    row << testRange.collectMany { c2 ->
      (aChar..c2).collect { new Tuple(c2, it) }
    }
    c = row[0]
    rowChar = row[1]
  }
  // end::overspecified-padding[]
}
