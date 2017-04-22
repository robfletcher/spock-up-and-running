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

package squawker.client.step3;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import squawker.client.SquawkerApi;

public class TimelineStream {

  // tag::scheduler-seam[]
  private final Scheduler scheduler; // <1>
  private final int interval;
  private final TimeUnit intervalUnit;
  private final SquawkerApi squawker;
  private final String username;

  public TimelineStream(Scheduler scheduler,
                        String username,
                        int interval,
                        TimeUnit intervalUnit, SquawkerApi squawker) {
    this.scheduler = scheduler;
    this.username = username;
    this.interval = interval;
    this.intervalUnit = intervalUnit;
    this.squawker = squawker;
  }

  public void start() {
    Observable
      .interval(interval, intervalUnit, scheduler) // <2>
      .map(tick -> squawker.getTimeline(username, null))
      .subscribe(System.out::println);
  }
  // end::scheduler-seam[]
}
