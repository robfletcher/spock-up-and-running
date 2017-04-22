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

import spock.lang.*

class LifecycleSpec extends Specification {

  // tag::print-lifecycle[]
  def setupSpec() {
    println "> setupSpec"
  }

  def cleanupSpec() {
    println "> cleanupSpec"
  }

  def setup() {
    println "-> setup"
  }

  def cleanup() {
    println "-> cleanup"
  }

  def "feature method 1"() {
    println "--> feature method 1"
    expect:
    2 * 2 == 4
  }

  def "feature method 2"() {
    println "--> feature method 2"
    expect:
    3 * 2 == 6
  }
  // end::print-lifecycle[]

}
