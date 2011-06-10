/*
 * Copyright 2011 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.learn.cxf;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.OptionUtils.combine;
import static org.ops4j.pax.tinybundles.core.TinyBundles.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestAddress;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.apart.DoSomething;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.container.PaxExamRuntime;
import org.ops4j.pax.exam.testforge.BundlesInState;
import org.ops4j.pax.exam.testforge.SingleClassProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

/**
 * Here's a somewhat different test setup that is made to elaborate on foreign
 * technology to be tested on a given target framework. So here we do care on
 * 
 * - the set of bundles loaded
 * - single target framework
 *  - not really about the probe but on being able to interactively "play" with the framework (shell)
 * - Creating companion bundles "at will" using tinybundles.
 * 
 * @author Toni Menzel
 * 
 */
@RunWith( JUnit4TestRunner.class )
public class CXFLaboratories {
	
	@Configuration
	public Option[] configure() {
		return options(
				systemProperty( "org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
				
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
				//mavenBundle().groupId( "org.ops4j.pax.url").artifactId("pax-url-link" ).version( "1.3.2" ),
				//mavenBundle().groupId( "org.ops4j.pax.url").artifactId("pax-url-aether" ).version( "1.3.2" ),
				mavenBundle().groupId( "org.ops4j.pax.tinybundles" ).artifactId( "pax-tinybundles-core" ).version( "1.0.0-SNAPSHOT" ),
				
				provision( bundle( with() )
				        .set( "Import-Package", "org.apache.cxf.bus.spring,org.slf4j" )
				        .set("DynamicImport-Package","*")
						.add (DoSomething.class )
						.add( "META-INF/spring/blablub.xml", getClass().getResource("/org/ops4j/pax/exam/apart/spring.xml") )
						.build() )
		);
	}
	
	@Test
    public void manualTest( BundleContext ctx )
    {
        
    }
	
	@Test
	public TestAddress saneLoggerFactory( TestProbeBuilder builder )
	{
       return builder.addTest( SingleClassProvider.class, (Object)LoggerFactory.class.getName() );
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
		ExamSystem system = PaxExamRuntime.createServerSystem(
				combine(new CXFLaboratories().configure(),options( 
				        // those two bundles are loaded automatically when using the test container.
				        // but you don't get anything pre-loaded in "Server Mode".
				        mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-api").version("1.6.1"),
				        mavenBundle().groupId("org.osgi").artifactId("org.osgi.compendium").version("4.2.0"),
                        
						mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.runtime").version(gogoVersion),
						mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.shell").version(gogoVersion),
						mavenBundle().groupId("org.apache.felix").artifactId("org.apache.felix.gogo.command").version(gogoVersion),				
						workingDirectory( System.getProperty( "user.dir") +  "/target/server" )
				) ) );
		
		TestContainer container = PaxExamRuntime.createContainer( system );
		container.start();
		// Container will be stopped by user when main exits (either by issuing shell commands or hitting the kill button.
	}
}
