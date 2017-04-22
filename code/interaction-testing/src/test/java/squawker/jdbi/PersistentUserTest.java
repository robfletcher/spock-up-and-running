/*
 * Copyright 2013 the original author or authors.
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
package squawker.jdbi;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;
import squawker.User;
import static java.time.Instant.now;

public class PersistentUserTest {

  @Rule public final JUnitRuleMockery context = new JUnitRuleMockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE);
    }
  };

  DataStore dataStore = context.mock(DataStore.class);
  User user = new PersistentUser(dataStore, "spock", now());

  // tag::jmock[]
  @Test
  public void followingAnotherUserIsPersisted() {
    final User other = new User("kirk");

    context.checking(new Expectations() {
      {
        oneOf(dataStore).follow(user, other);
      }
    });

    user.follow(other);

    context.assertIsSatisfied();
  }
  // end::jmock[]
}
