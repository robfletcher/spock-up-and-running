/*
 * Copyright 2014 the original author or authors.
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
package squawker.registration;

import org.jmock.*;
import org.jmock.integration.junit4.*;
import org.jmock.lib.legacy.*;
import org.junit.*;
import org.junit.experimental.theories.*;
import org.junit.runner.*;
import squawker.*;
import squawker.jdbi.*;
import static org.junit.Assert.*;

// tag::junit-theory[]
@RunWith(Theories.class)
public class RegistrationTheory {

  @DataPoints
  public static String[] invalidUsernames = {null, "", "    ", "@&%$+[", "spock"}; // <1>

  @Rule public final JUnitRuleMockery context = new JUnitRuleMockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE);
    }
  };

  private DataStore dataStore = context.mock(DataStore.class);
  private RegistrationService service = new RegistrationService(dataStore);

  @Theory
  public void cannotRegisterWithAnInvalidUsername(String username) { // <2>
    context.checking(new Expectations() {
      {
        allowing(dataStore).usernameInUse("spock"); will(returnValue(true));
        never(dataStore).insert(with(any(User.class)));
      }
    });

    try {
      service.register(username);
      fail("Should have thrown RegistrationException"); // <3>
    } catch (RegistrationException e) {
      // expected
    }
  }
}
// end::junit-theory[]