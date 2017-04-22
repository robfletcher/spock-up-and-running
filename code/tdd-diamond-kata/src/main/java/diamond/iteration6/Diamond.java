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

package diamond.iteration6;

import java.util.*;
import java.util.function.*;

public class Diamond implements Function<Character, List<String>> {

  @Override public List<String> apply(Character c) {
    if (c < 'A' || c > 'Z') {
      throw new IllegalArgumentException(c + " is outside the valid range A..Z");
    } else {
      final int midpoint = c - 'A'; // <1>

      List<String> result = new ArrayList<>();
      for (char nextChar = 'A'; nextChar <= c; nextChar++) {
        final int x = midpoint - result.size(); // <2>
        StringBuilder row = new StringBuilder();
        while (row.length() < x) { // <3>
          row.append('-');
        }
        row.append(nextChar); // <4>
        while (row.length() <= midpoint) { // <5>
          row.append('-');
        }

        row.append(new StringBuilder(row.substring(0, midpoint)).reverse()); // <6>

        result.add(row.toString());
      }

      int index = result.size();
      for (int i = 0; i < (index - 1); i++) {
        result.add(index, result.get(i));
      }

      return result;
    }
  }
}
