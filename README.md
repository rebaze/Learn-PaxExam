# Welcome to the Pax Exam Learning Project

We assume you are familar with the basic principles of OSGi.
Otherwise, here are some interesting links to get to know OSGi any better:

* <http://osgi.org>
* <http://njbartlett.name>
* <http://www.osgi.org/Planet/HomePage> (feed)

Also, we assume you are somewhat used to work with Maven based projects and know how to import them properly into your IDE of choice.

#### Some recommendations

* IntelliJ IDEA, you can import the pom.xml straight into the IDE
* For Eclipse, use the latest M2Eclipse or Sonatypes Maven IDE

This does not mean you can only use Maven, but most sessions are maven based projects, so you should be able to read, execute and modify them.

# Help

You will get Open Source Support & Help here:

* OPS4J Wiki: <http://paxexam.ops4j.org>
* OPS4J Mailing List: <http://lists.ops4j.org/mailman/listinfo>
* OPS4J IRC Channel on freenode.org #ops4j
* Issues and Releasenotes on <http://issues.ops4j.org/browse/PAXEXAM>

# How to get started

We suggest you to clone this project as follows:

    git clone git://github.com/tonit/Learn-PaxExam.git

And import it into your IDE of choice.
Then, read the primer, below.

# A Pax Exam Primer

Here you will get introduces into the most basic concepts and "thinking" of Pax Exam.

Pax Exam is a test framework for OSGi bundles and OSGi based applications.
Its based on the principles of launching a specified OSGi Framework setup.
One "setup" can consist of multiple frameworks (lets say Felix, Equinox or Knopflerfish),
a set of bundles and options that being installed upon startup.

After startup, your test - we call that "probe" - will be installed. Also as a Bundle. (thats the unit of deployment for OSGi).

At this point, you should not care to much how a probe really looks like physically.

You should translate Probe with: "My Test classes bundled up on the fly with Manifest that i should not care about".

Once your tests are installed in the Test Container, they are simply instantiated and "invoked".

Because your test could end up in a totally different JVM (read Pax Runner Container below) you should be aware of the following:

* A probe carries your tests only. Everything that is not in your "test-classes" + used inside your test-method, you need to have that provided by another bundle.

* You may optain context information via Parameter injection. See the Code Examples linked below for clarification. Its really simple.


# Options

The option system is quite unique to Pax Exam and is crucial to understand it.
Simply put, you will configure your Test Setup (which includes which bundles you want to load, perhaps which frameworks (depends Test Container choice), etc.)
programatically via static method calls, that are imported "statically" so it give you quite distraction-free, DSL-like configuration options with full IDE-code completion support.

You add an import like this to the class that configures your test setup (this might be the Test Class itself or it might be entirely elsewhere, you can put it like you need it. Its simple Java laws).

    import static org.ops4j.pax.exam.CoreOptions.*;
    import static org.ops4j.pax.exam.LibraryOptions.*;

This will give you the chance to use one or more of the factory methods. They all will return something that is assignable to

    org.ops4j.pax.exam.Option
    
You will use a collection of this type via passing it to the TestContainer at construction time.
Again, this is knowledge that you should have heard once, depending on how you will use Pax Exam you might not really care about where Options are used.

What matters is, that the following imports might be helpful to discover options for your Test Case and eventually use them.
Note that those classes live either in Pax Exam API (artifactId=pax-exam) or in Container specific artifacts like the following:

    org.ops4j.pax.exam.container.def.PaxRunnerOptions
    
The class above comes from the PaxRunner-TestContainer implementation. Thus, it contains options that are just recognized when using the PaxRunner-TestContainer.
Read below on how to "select" a test container implementation or just look at the tutorials at <https://github.com/tonit/Learn-PaxExam>.

Those options form a lose collection (as mentioned). It does not necessarily mean that all options will be recognized or supported. 
Its just a (the) way to configure Pax Exam.

# Test Containers

Currently Pax Exam ships with two really different TestContainer implementations:

* PaxRunnerTestContainer

* NativeTestContainer

You need to decide for one or other and put it into your test-projects classpath.
In maven you do either this:

     <dependency>
         <groupId>org.ops4j.pax.exam</groupId>
         <artifactId>pax-exam-container-native</artifactId>
         <version>${paxexamversion}</version>
         <scope>test</scope>
     </dependency>

or this:

     <dependency>
         <groupId>org.ops4j.pax.exam</groupId>
         <artifactId>pax-exam-container-paxrunner</artifactId>
         <version>${paxexamversion}</version>
         <scope>test</scope>
     </dependency>


#### Pax Runner Test Container

This is what you know if you've used Pax Exam 1.x.
Internally, its been stripped down heavily, parts moved out of this implementation into Pax Exams core so other containers can the functionality.

This container will launch a new Pax Runner instance as "TestContainer", which means your test will end up in a different JVM !
So when debugging, you'll need to enable Remote Debugging in your IDE.

Benefits:

* New JVM means you have full control over which JVM will be used and what the options look like

* Vast amount of additional options: use scan*() and profile() options which simplify your setup.


Drawbacks:

* May be slower (new Process launched). More computation happening crunching the arguments forth and back.

* RMI Communication happening. This has been stabilized compared to Pax Exam 1. But its still networking with all its implications.

Use it:

 * when you need the Pax Runner specialties

#### Native Test Container

A brand new Test Container using OSGi Core 4.2 Launcher API.
This means, you can simply add at least one compatible OSGi framework to your Test Setup like so:

     <dependency>
         <groupId>org.apache.felix</groupId>
         <artifactId>org.apache.felix.framework</artifactId>
         <version>${felixversion}</version>
         <scope>test</scope>
     </dependency>

The Testcontainer will detect it ( or them, you may add more framework vendors) and launch it using the common API thanks to the launcher api standard.

Benefits:

* Really fast

* Flawless debugging experience

Drawbacks:

 * Less options compared to the Pax Runner Test Container.

 * Even if we could, this test container will not try to copy all the options the Pax Runner Container gives you. (like the profiles/scanner feature)

Use it:

 * by default. You don't get less options but also a slimmer call-stack. Less things that could go wrong.

# Code Examples

After reading the Primer section above (at least) you are ready to go hands-on with the code examples that reside in this "learn pax exam" project.
Here's what you find and the recommended order of reading:

* <https://github.com/tonit/Learn-PaxExam/tree/master/lesson-plumbing>

* <https://github.com/tonit/Learn-PaxExam/tree/master/lesson-porcelain>

* <https://github.com/tonit/Learn-PaxExam/tree/master/lesson-junit>

* <https://github.com/tonit/Learn-PaxExam/tree/master/lesson-player>


When reading the examples, its best to have a focus on:

* the pom.xml to learn about which container is used (you may switch that!) and other dependencies

* the Test Sources, which are documented extensively.

  
# Differences to Pax Exam 1.x

For switchers, here are some words about what has changed from Pax Exam 1.x.

On the inside, Pax Exam 2 is an almost 80% rewrite of the "host" side. Which means, the workflow of how tests are gathered, constructed, Test Containers spawned, this has completely changed.
With a focus on:

* Stability

* Ease for extension

* Documentation

As a user of Exam 1.x, you should be able to completely transition to Exam 2 with just changing the dependencies in your pom.xml. (or build.gradle)
If you used Pax Exam API previously heavily, you might need to change some package-imports in your setup classes.

What will not work:

* Pax Exam Maven Plugin: This has been left out for future releases. Reason: it ties you to use the Pax Runner Test Container only.

