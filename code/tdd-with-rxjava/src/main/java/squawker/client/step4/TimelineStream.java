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

package squawker.client.step4;

import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import squawker.Message;
import squawker.client.SquawkerApi;

public class TimelineStream {

  private final Scheduler scheduler;
  private final int interval;
  private final TimeUnit intervalUnit;
  private final SquawkerApi squawker;
  private final String username;

  public TimelineStream(Scheduler scheduler,
                        String username,
                        int interval,
                        TimeUnit intervalUnit,
                        SquawkerApi squawker,
                        Action1<List<Message>> subscriber) {
    this.scheduler = scheduler;
    this.username = username;
    this.interval = interval;
    this.intervalUnit = intervalUnit;
    this.squawker = squawker;
    this.subscriber = subscriber;
  }

  // tag::subscriber[]
  private final Action1<List<Message>> subscriber;

  public void start() {
    Observable
      .interval(interval, intervalUnit, scheduler)
      .map(tick -> squawker.getTimeline(username, null))
      .subscribe(subscriber);
  }
  // end::subscriber[]
}
