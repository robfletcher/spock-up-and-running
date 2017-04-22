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

package squawker.client.step1;

import java.util.concurrent.TimeUnit;
import squawker.client.SquawkerApi;

// tag::skeleton[]
public class TimelineStream {

  private final String username; // <1>
  private final int interval; // <2>
  private final TimeUnit intervalUnit;
  private final SquawkerApi squawker; // <3>

  public TimelineStream(String username,
                        int interval,
                        TimeUnit intervalUnit,
                        SquawkerApi squawker) {
    this.username = username;
    this.interval = interval;
    this.intervalUnit = intervalUnit;
    this.squawker = squawker;
  }

  public void start() {
    // <4>
  }
}
// end::skeleton[]
