package com.revenat.jmemcached.client.impl;

import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;

/**
 * This interface represents parameter object for {@link DefaultClient} component.
 * 
 * @author Vitaly Dragun
 *
 */
interface ClientContext {
	
	/**
	 * Returns {@link RequestWriter} instance.
	 */
	RequestWriter getRequestWriter();
	
	/**
	 * Returns {@link ResponseReader} instance.
	 */
	ResponseReader getResponseReader();
	
	/**
	 * Returns {@link ObjectSerializer} instance.
	 */
	ObjectSerializer getObjectSerializer();
	
	/**
	 * Returns {@link ObjectDeserializer} instance.
	 */
	ObjectDeserializer getObjectDeserializer();
}
