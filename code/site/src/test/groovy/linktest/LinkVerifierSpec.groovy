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

class LinkVerifierSpec extends Specification {

  @Shared rootDir = new File("build/jbake") // TODO: better way to supply this
  @Shared Set<URI> links = new HashSet<>()

  void setupSpec() {
    assert rootDir.isDirectory()

    rootDir.traverse(nameFilter: ~/.*\.html/) { file ->
      $(file.text).find("a")*.attr("href").each { href ->
        if (href.toURI().absolute) {
          links << href.toURI()
        } else if (!href.startsWith("#")) {
          links << new File(file.parentFile, href).toURI()
        }
      }
    }
  }

  @Unroll("link to '#link' is valid")
  def "site external links are valid"() {
    expect:
    Unirest.head(link.toString()).asBinary().status == HTTP_OK

    where:
    link << links.findAll { it.scheme == "http" }
  }

  @Unroll("link to '#link' is valid")
  def "site internal links are valid"() {
    expect:
    new File(link).exists()

    where:
    link << links.findAll { it.scheme == "file" }
  }

}
