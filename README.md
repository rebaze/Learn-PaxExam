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


#### Native Test Container


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
