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

import java.util.*;
import org.jmock.*;
import org.jmock.integration.junit4.*;
import org.jmock.lib.legacy.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import squawker.*;
import squawker.jdbi.*;
import static org.junit.runners.Parameterized.*;

// tag::junit-parameterized[]
@RunWith(Parameterized.class)
public class RegistrationParameterizedTest {

  @Parameters
  public static Collection<Object[]> invalidUsernames() { // <1>
    Object[][] data = new Object[][] {
      {null}, {""}, {"    "}, {"@&%$+["}, {"spock"}
    };
    return Arrays.asList(data);
  }

  @Rule public final JUnitRuleMockery context = new JUnitRuleMockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE);
    }
  };

  private DataStore dataStore = context.mock(DataStore.class);
  private RegistrationService service = new RegistrationService(dataStore);
  private String username; // <2>

  public RegistrationParameterizedTest(String username) { // <3>
    this.username = username;
  }

  @Test(expected = RegistrationException.class)
  public void cannotRegisterWithAnInvalidUsername() {
    context.checking(new Expectations() {
      {
        allowing(dataStore).usernameInUse("spock"); will(returnValue(true));
        never(dataStore).insert(with(any(User.class)));
      }
    });

    service.register(username); // <4>
  }
}
// end::junit-parameterized[]
