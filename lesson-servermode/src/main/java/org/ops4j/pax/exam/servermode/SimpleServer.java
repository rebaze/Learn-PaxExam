package org.ops4j.pax.exam.servermode;

import static org.ops4j.pax.exam.CoreOptions.*;

import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.spi.container.PaxExamRuntime;

public class SimpleServer
{
    
    /**
     * When creating a longer running process where you want to interact with the system manually (lets say by using a shell or web console),
     * you should do that in runner that does not shutdown threads automatically when the method exits.
     * A Main Entry is a good example. A @Test using JUnit Testrunner would be a bad example because it will shutdown the framework.
     * 
     * The only thing to do differently than usual (lets say to lesson-plumbing) is to use the "createServerSystem" factory method instead of "createTestSystem".
     * Technically you could use the other factory, but you would end up with the additional Pax Exam bundles in your process. 
     * The ServerSysten factory also sets some default options like "keepOriginalUrls" and "keepCaches". 
     * You see, thise is just candy, you can still use the "createTestSystem" method and put together the options yourself.
     * 
     * Another quite important option you should set when considering a server like process kicked of by pax exam is to set the workingDirectory manually.
     * If you do not set this, pax exam will create a unique temporary folder for you. This may or may not be desired.
     * 
     */
    public static void main( String[] args )
            throws Exception
    {
        // The usual configuration
        String gogoVersion = "0.8.0";
        Option[] options = options(
             // those two bundles are loaded automatically when using the test container.
                // but you don't get anything pre-loaded in "Server Mode".
                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-api").version("1.6.1"),
                mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.2.0"),
                
                mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.runtime").version(gogoVersion),
                mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.shell").version(gogoVersion),
                mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.command").version(gogoVersion),             
                workingDirectory( System.getProperty( "user.dir") +  "/target/server" )
        );
        
        
        // create a proper ExamSystem with your options. Focus on "createServerSystem"
        ExamSystem system = PaxExamRuntime.createServerSystem( options );
        
        // create Container (you should have exactly one configured!) and start.
        TestContainer container = PaxExamRuntime.createContainer( system );
        container.start();
        
    }
}
