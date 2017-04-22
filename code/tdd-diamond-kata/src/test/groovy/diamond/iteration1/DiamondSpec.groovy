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

package diamond.iteration1

import java.util.function.Function
import spock.lang.Shared
import spock.lang.Unroll

class DiamondSpec extends diamond.iteration0.DiamondSpec {

  @Override
  Function<Character, List<String>> createDiamond() {
    new Diamond()
  }

  // tag::diamond-a[]
  def "The diamond of A is 'A'"() {
    expect:
    diamond.apply(aChar) == ["A"]
  }
  // end::diamond-a[]

  // tag::diamonds-are-square[]
  @Shared char bChar = 'B'
  @Shared Range<Character> testRange = bChar..zChar // <1>

  @Unroll
  def "diamond('#c') is square"() {
    given:
    def result = diamond.apply(c)

    expect:
    result.every {
      it.length() == result.size()
    }

    where:
    c << testRange // <2>
  }
  // end::diamonds-are-square[]
}
