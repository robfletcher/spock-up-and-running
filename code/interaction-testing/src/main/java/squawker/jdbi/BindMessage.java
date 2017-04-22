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

import java.lang.annotation.*;
import org.skife.jdbi.v2.*;
import org.skife.jdbi.v2.sqlobject.*;
import squawker.*;

@BindingAnnotation(BindMessage.MessageBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindMessage {
  public static class MessageBinderFactory implements BinderFactory {
    public Binder build(Annotation annotation) {
      return new Binder<BindMessage, Message>() {
        public void bind(SQLStatement q, BindMessage bind, Message message) {
          q.bind("text", message.getText());
          q.bind("postedAt", message.getPostedAt());
          q.bind("username", message.getPostedBy().getUsername());
        }
      };
    }
  }
}
