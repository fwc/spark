/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql

import org.apache.spark.SparkConf

/**
 * A common trait for test suites that require a [[SparkSession]]. It abstracts over the
 * different session types supported by Spark tests: Spark classic sessions, Hive sessions
 * (backed by [[org.apache.spark.sql.hive.test.TestHiveContext]]), and Spark Connect sessions.
 * Concrete implementations are responsible for managing the session lifecycle (creation,
 * configuration, and teardown).
 */
trait SparkSessionProvider {
  protected def spark: SparkSession

  protected def sql(s: String): DataFrame = spark.sql(s)

  /**
   * Override this to configure the [[SparkConf]] of a test suite.
   * The result of [[sparkConf]] shall be used by the [[SparkSessionBinder]] when creating the
   * [[SparkSession]] provided as [[spark]].
   *
   * Example Usage:
   * {{{
   *   FooSuite extends ... {
   *
   *     // set suite-wide conf by overriding sparkConf
   *     override protected def sparkConf: SparkConf = super.sparkConf
   *       .set("spark.someConfThatShouldBeTrueInFooSuite", "true")
   *       .set("spark.anotherConfThatShouldBeFalse", "false")
   *
   *     test("some testcase") {
   *       // use withConf for localized conf overrides of [[RuntimeConfig]]
   *       withConf("spark.someTestcaseSpecificConf" -> "42") {
   *         // ...
   *       }
   *     }
   *   }
   * }}}
   */
  protected def sparkConf: SparkConf = new SparkConf()
}
