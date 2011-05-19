package org.ops4j.pax.exam.apart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoSomething {
	private static Logger LOG = LoggerFactory.getLogger( DoSomething.class );
	
	static {
		LOG.warn("Hello World!");
	}
	
	public void shutdown() {
		LOG.info("Goodbye World!");
	}
}
