package com.revenat.jmemcached.client.impl;

import com.revenat.jmemcached.client.ClientConfig;
import com.revenat.jmemcached.exception.JMemcachedConfigException;
import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;
import com.revenat.jmemcached.protocol.impl.ObjectConverter;
import com.revenat.jmemcached.protocol.impl.RequestConverter;
import com.revenat.jmemcached.protocol.impl.ResponseConverter;

/**
 * Default implementation of the {@link ClientConfig}
 * 
 * @author Vitaly Dragun
 *
 */
class DefaultClientConfig implements ClientConfig {
	private final String host;
	private final int port;
	private final RequestWriter requestWriter;
	private final ResponseReader responseReader;
	private final ObjectSerializer objectSerializer;
	private final ObjectDeserializer objectDeserializer;
	
	DefaultClientConfig(String host, int port) {
		this.host = validateHost(host);
		this.port = validatePort(port);
		this.requestWriter = new RequestConverter();
		this.responseReader = new ResponseConverter();
		ObjectConverter converter = new ObjectConverter();
		this.objectSerializer = converter;
		this.objectDeserializer = converter;
	}

	private static String validateHost(String host) {
		if (host == null) {
			throw new NullPointerException("host can not be null");
		}
		if (host.trim().isEmpty()) {
			throw new JMemcachedConfigException("host can not be empty");
		}
		
		return host;
	}
	
	private static int validatePort(int port) {
		if (port < 0 || port > 65535) {
			throw new JMemcachedConfigException(
					String.format("Invalid port number. Valid port number range is from 0 to 65535. Specified port value: %d",
							port));
		}
		return port;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public RequestWriter getRequestWriter() {
		return requestWriter;
	}

	@Override
	public ResponseReader getResponseReader() {
		return responseReader;
	}

	@Override
	public ObjectSerializer getObjectSerializer() {
		return objectSerializer;
	}

	@Override
	public ObjectDeserializer getObjectDeserializer() {
		return objectDeserializer;
	}
}
