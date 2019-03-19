package com.revenat.jmemcached.client;

import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;

/**
 * Component responsible for storing all the client-specific configurations
 * for the {@code JMemcached} application.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ClientConfig {

	/**
	 * Returns host name of the {@code JMemcached} server.
	 */
	String getHost();
	
	/**
	 * Returns port number of the {@code JMemcached} server.
	 * @return
	 */
	int getPort();
	
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
