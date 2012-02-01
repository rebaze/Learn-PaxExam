/*
 * Copyright (C) 2011 Toni Menzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.lesson1;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.TestAddress;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.TestProbeProvider;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.spi.container.PaxExamRuntime.*;

/**
 * This is the very first Pax Exam Lesson.
 * It shows you Pax Exam's first, and most rough - down to earth - API. The so called Plumbing API.
 * You recognize it like so:
 * 1. No @RunWith annotation on the Test Class. So plain JUnit. You can even use TestNG or a Main class. Or any other means of running java.
 * 2. You use TestContainer lifecycle methods like start() and stop() and install().
 * 3. You need to put together the probe "yourself" (select class and method).
 *
 * The good parts:
 * + Its how things are: You have a TestContainer that has a lifecycle. You make a probe and invoke tests (from probe) on a (running) TestContainer instance.
 * + Enables higher level abstraction.
 *
 * The bad parts:
 * - fairly low level (lifecycle)
 * - hand roll your tests in another class
 * - you don't really see failures of a single test container or test. Its one "big" method for the JUnit Runner.
 *
 * Watch: <VIDEOLINK> for an hands-on description.
 */
public class LessonTest {


    @Test
    public void testLesson1Unit1()
        throws Exception
    {
        /**
         * Options. A KEY part of the Pax Exam API. Those are static factory methods derived from:
         * org.ops4j.pax.exam.CoreOptions
         * org.ops4j.pax.exam.LibraryOptions
         * etc.
         *
         * This is how you configure your test setup.
         * The only thing where some defaults may come from too is your classpath.
         * So have a look at this projects pom.xml for some insight what choices you get from there.
         *
         */
    	ExamSystem system = createTestSystem( 
            allFrameworksVersions(),
            equinox()
        );

        TestProbeProvider p = makeProbe(system);

        /**
         * Here's where Pax Exam comes to life: you get a {@link TestContainerFactory}
         * usually via {@link org.ops4j.pax.exam.spi.container.PaxExamRuntime#getTestContainerFactory()} .
         * This looks up appropriate classes in your classpath.
         * By having either "pax-exam-container-native" or "pax-exam-container-paxrunner" in your pom.xml/test-classpath,
         * you will get the desired container factory.
         * The "create" call creates one or more TestContainer instances.
         * Why is that ?
         * Well, depending on which container you select and what options you pass in, you might get more than one physical container.
         * In this example (pom.xml includes the pax-exam-container-native) you will get 2 Test Container instances.
         * Because: NativeTestContainerFactory uses the OSGi Core 4.2 Launcher API to launch OSGi frameworks.
         * In turn, you need to have (additionally) at least one compatible OSGi Framework dependency in your pom.
         * This project has both, Felix & Equinox as dependency. This is what NativeTestContainerFactory will find out.
         * This is how you end up with two container instances here. Likewise you'll need to iterate over them.
         *
         * The rest is simple lifecycle management:
         * - starting (incorporates all options you passed in at construction time),
         * - installing the extra bundle (probe!)
         * - invoking all tests that have been added to the probe before
         * - stop
         *
         */
        for( TestContainer testContainer : getTestContainerFactory().create( system ) ) {
            try {
                testContainer.start();
                testContainer.install( p.getStream() );
                for( TestAddress test : p.getTests() ) {
                    testContainer.call( test );
                }
            } finally {
                testContainer.stop();
            }
        }
    }

    /**
     * Here's how you make a probe. As mentioned in {@link Probe} its a bundle that is computed on the fly.
     * You - as a user - just add "tests", and invoke "build() at the very end. You will end up with something ({@link TestProbeProvider})
     * where you get the physical bundle ({@link org.ops4j.pax.exam.TestProbeProvider#getStream()}) from.
     *
     * @return the Probe, really do used.
     *
     * @throws java.io.IOException Problems
     */
    private TestProbeProvider makeProbe(ExamSystem system)
        throws IOException
    {
    	
        TestProbeBuilder probe = system.createProbe( new Properties() );
        probe.addTest( Probe.class, "probe1" );

        probe.addTest( Probe.class, "probe2" );

        // passing parameters.
        probe.addTest( Probe.class, "probe3", "Parameter" );


        return probe.build();
    }
}
