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

package diamond.iteration2

import java.util.function.Function
import spock.lang.Unroll

class DiamondSpec extends diamond.iteration1.DiamondSpec {

  @Override
  Function<Character, List<String>> createDiamond() {
    new Diamond()
  }

  // tag::diamond-height[]
  @Unroll("diamond(#c) should have #expectedHeight rows")
  def "a diamond's height is determined by the character argument"() {
    given:
    def result = diamond.apply(c)

    expect:
    result.size() == expectedHeight

    where:
    c << testRange // <1>
    expectedHeight = ((c - aChar) * 2) + 1 // <2>
  }
  // end::diamond-height[]

}
