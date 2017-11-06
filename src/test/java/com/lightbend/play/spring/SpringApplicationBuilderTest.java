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

package com.lightbend.play.spring;

import javax.inject.Provider;
import javax.inject.Singleton;

import org.junit.Test;
import play.api.Application;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpringApplicationBuilderTest {

  static class Foo {
    static boolean created = false;
    Foo() { created = true; }
  }

  static class FooProvider implements Provider<Foo> {
    public Foo get() {
      return new Foo();
    }
  }

  @Test
  public void testSingletonProvider() {
    Foo.created = false;

    SpringApplicationBuilder builder = new SpringApplicationBuilder()
        .bindings(new Module() {
          public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
            return seq(
                bind(Foo.class).toProvider(FooProvider.class).in(Singleton.class)
            );
          }
        });
    Application app = builder.build();

    // check eagerness
    assertTrue(Foo.created);

    // check singleton property
    assertEquals(app.injector().instanceOf(Foo.class), app.injector().instanceOf(Foo.class));
  }

}
