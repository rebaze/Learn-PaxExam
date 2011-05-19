package org.ops4j.pax.exam.learn.cxf;

import static org.ops4j.pax.exam.LibraryOptions.easyMockBundles;
import static org.ops4j.pax.exam.LibraryOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.OptionUtils.*;
import static org.ops4j.pax.tinybundles.core.TinyBundles.*;
import org.ops4j.pax.tinybundles.core.TinyBundles;

import static org.ops4j.pax.exam.spi.container.PaxExamRuntime.getTestContainerFactory;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestAddress;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.TestProbeProvider;
import org.ops4j.pax.exam.apart.DoSomething;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.container.PaxExamRuntime;
import org.ops4j.pax.exam.spi.container.PlumbingContext;
import org.ops4j.pax.exam.testforge.BundlesInState;
import org.ops4j.pax.exam.testforge.SingleClassProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Here's a somewhat different test setup that is made to elaborate on foreign
 * technology to be tested on a given target framework. So here we do care on
 * 
 * 
 * -- the set of bundles loaded
 * 
 * -- single target framework
 * 
 * -- not really about the probe but on being able to interactively "play" with
 * the framework (shell)
 * 
 * -- Creating companion bundles "at will" using tinybundles.
 * 
 * @author tonit
 * 
 */
@RunWith( JUnit4TestRunner.class )
public class CXFLaboratories {
	
	private static Logger LOG = LoggerFactory.getLogger(CXFLaboratories.class.getName());
	
	@Configuration
	public Option[] configure() {
		return options(
				systemProperty( "org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
				
				
				//mavenBundle().groupId("org.apache.aries.blueprint").artifactId("org.apache.aries.blueprint").version("0.3"),
				//mavenBundle().groupId("org.apache.aries.proxy").artifactId("org.apache.aries.proxy").version("0.3"),
				//mavenBundle().groupId("org.apache.aries").artifactId("org.apache.aries.util").version("0.3"),
				mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version("1.0.3"),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.asm").version("3.3_2"),
				
				mavenBundle().groupId("org.apache.cxf").artifactId("cxf-bundle").version("2.4.0"),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.javax.mail").version("1.4.1_4"),
				mavenBundle().groupId("org.apache.geronimo.specs").artifactId("geronimo-stax-api_1.2_spec").version("1.0"),
				mavenBundle().groupId("org.apache.geronimo.specs").artifactId("geronimo-activation_1.1_spec").version("1.1"),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.wsdl4j").version("1.6.2_4"),
				mavenBundle().groupId("org.apache.neethi").artifactId("neethi").version("3.0.0"),
				mavenBundle().groupId("org.apache.ws.xmlschema").artifactId("xmlschema-core").version("2.0"),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.xmlresolver").version("1.2_4"),
				
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.aopalliance").version("1.0_5"),
				
				mavenBundle().groupId("org.springframework").artifactId("spring-core").version("3.0.5.RELEASE"),
				mavenBundle().groupId("org.springframework").artifactId("spring-asm").version("3.0.5.RELEASE"),
				mavenBundle().groupId("org.springframework").artifactId("spring-expression").version("3.0.5.RELEASE"),
				mavenBundle().groupId("org.springframework").artifactId("spring-beans").version("3.0.5.RELEASE"),
				mavenBundle().groupId("org.springframework").artifactId("spring-aop").version("3.0.5.RELEASE"),
				mavenBundle().groupId("org.springframework").artifactId("spring-context").version("3.0.5.RELEASE"),
				mavenBundle().groupId("org.springframework").artifactId("spring-context-support").version("3.0.5.RELEASE"),
								
				mavenBundle().groupId("org.springframework.osgi").artifactId("spring-osgi-io").version("1.2.1"),
				mavenBundle().groupId("org.springframework.osgi").artifactId("spring-osgi-core").version("1.2.1"),
				mavenBundle().groupId("org.springframework.osgi").artifactId("spring-osgi-extender").version("1.2.1"),
				mavenBundle().groupId("org.springframework.osgi").artifactId("spring-osgi-annotation").version("1.2.1"),
				
				provision( bundle( withBnd() )
						.add (DoSomething.class )
						.add( "META-INF/spring/spring.xml", getClass().getResource("/org/ops4j/pax/exam/apart/spring.xml") )
						.build() )
		);
	}
	
	@Test
	public void runTest( BundleContext ctx )  {
		
	}
	
	//@Test
	public TestAddress saneLoggerFactory( TestProbeBuilder builder )
	{
       return builder.addTest( SingleClassProvider.class, (Object)LoggerFactory.class.getName() );
	}
	
	@Test
	public TestAddress sane( TestProbeBuilder builder )
	{
       return builder.addTest( SingleClassProvider.class, (Object)BundleActivator.class.getName() );
	}
	
	@Test
	public TestAddress allBundlesResolve( TestProbeBuilder builder )
	{
       return builder.addTest( BundlesInState.class, Bundle.ACTIVE );
	}
	
	/**
	 * Here's our little Pax Exam driven OSGi Server running our config PLUS some additional tools for interactive inspection (shell)
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// add gogo set to the mix.
		String gogoVersion = "0.8.0";
		TestContainer container = PaxExamRuntime.createContainer( 
				combine(new CXFLaboratories().configure(),options( 
								mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.runtime").version(gogoVersion),
								mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.shell").version(gogoVersion),
								mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.command").version(gogoVersion)							
				) ) );
		
			container.start();
			// Container will be stopped by user when main exits (either by issuing shell commands or hitting the kill button.
	}
}
