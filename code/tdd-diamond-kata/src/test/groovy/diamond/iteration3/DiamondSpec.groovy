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

package diamond.iteration3

import java.util.function.Function
import spock.lang.Unroll

class DiamondSpec extends diamond.iteration2.DiamondSpec {

  @Override
  Function<Character, List<String>> createDiamond() {
    new Diamond()
  }

  // tag::naive-char-verification[]
  @Unroll
  def "the appropriate character appears in each row in the correct columns in diamond(#c)"() {
    given:
    def result = diamond.apply(c)

    expect:
    int lastIndex = result.size() - 1
    int midpoint = result.size().intdiv(2) // <1>

    for (rowChar in aChar..c) { // <2>
      int rowIndex = (rowChar - aChar) // <3>

      def topLeft = [x: midpoint - rowIndex, y: rowIndex] // <4>
      def topRight = [x: lastIndex - topLeft.x, y: rowIndex]
      def bottomLeft = [x: topLeft.x, y: lastIndex - rowIndex]
      def bottomRight = [x: topRight.x, y: bottomLeft.y]

      assert result[topLeft.y].charAt(topLeft.x) == rowChar // <5>
      assert result[topRight.y].charAt(topRight.x) == rowChar
      assert result[bottomLeft.y].charAt(bottomLeft.x) == rowChar
      assert result[bottomRight.y].charAt(bottomRight.x) == rowChar
    }

    where:
    c << testRange
  }
  // end::naive-char-verification[]
}
