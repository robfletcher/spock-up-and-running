/*
 * Copyright 2016 the original author or authors.
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

package squawker.test.e2e.pages2

import geb.Page
import squawker.test.e2e.pages1.MessageListModule

class TimelinePage extends Page {

  static url = "#/timeline"

  static atCheckWaiting = true
  static at = {
    $(".page-header").text() == "Timeline"
  }

  // tag::content-module[]
  static content = {
    timeline {
      $(".sq-message-list").module(MessageListModule)
    }
  }
  // end::content-module[]
}
