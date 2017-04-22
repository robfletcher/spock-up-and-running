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
package linktest

import com.mashape.unirest.http.*
import spock.lang.*
import static java.net.HttpURLConnection.*
import static jodd.jerry.Jerry.jerry as $

class LinkVerifierSpec1 extends Specification {

  // tag::link-verifier-v1[]
  // TODO: better way to supply this
  @Shared rootDir = new File("build/jbake")
  @Shared Set<String> links = new HashSet<>() // <1>

  void setupSpec() {
    rootDir.traverse(nameFilter: ~/.*\.html/) { file ->
      $(file.text).find("a")*.attr("href").each { href -> // <2>
        if (href.toURI().absolute) {
          links << href
        }
      }
    }
  }

  @Unroll("link to '#link' is valid")
  // <3>
  def "site external links are valid"() {
    expect:
    Unirest.head(link).asBinary().status == HTTP_OK // <5>

    where:
    link << links // <4>
  }
  // end::link-verifier-v1[]
}
