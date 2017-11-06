/*
 * Copyright 2017 Lightbend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lightbend.play.spring

import controllers.Assets
import play.api.ApplicationLoader.Context
import play.api._
import play.api.inject._
import play.core.WebCommands

/**
 * based on the awesome work of jroper:
 * https://github.com/jroper/play-spring
 */
class SpringApplicationLoader(protected val initialBuilder: SpringApplicationBuilder) extends ApplicationLoader {

  // empty constructor needed for instantiating via reflection
  def this() = this(new SpringApplicationBuilder)

  def load(context: Context) = {

    builder(context).build()
  }

  /**
   * Construct a builder to use for loading the given context.
   */
  protected def builder(context: ApplicationLoader.Context): SpringApplicationBuilder = {
    initialBuilder
      .in(context.environment)
      .loadConfig(context.initialConfiguration)
      .overrides(overrides(context): _*)
  }

  /**
   * Override some bindings using information from the context. The default
   * implementation of this method provides bindings that most applications
   * should include.
   */
  protected def overrides(context: ApplicationLoader.Context): Seq[Module] = {
    SpringApplicationLoader.defaultOverrides(context)
  }
}

private object SpringApplicationLoader {

  /**
   * The default overrides provided by the Scala and Java SpringApplicationLoaders.
   */
  def defaultOverrides(context: ApplicationLoader.Context) = {
    Seq(
      new Module {
        def bindings(environment: Environment, configuration: Configuration) = Seq(
          bind[OptionalSourceMapper] to new OptionalSourceMapper(context.sourceMapper),
          bind[WebCommands] to context.webCommands,
          bind[Assets].to[Assets],
          bind[play.Configuration].to[play.Configuration])
      })
  }

}
