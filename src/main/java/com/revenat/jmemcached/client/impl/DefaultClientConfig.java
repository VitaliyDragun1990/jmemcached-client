package com.revenat.jmemcached.client.impl;

import com.revenat.jmemcached.client.ClientConfig;
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
	private final RequestWriter requestWriter;
	private final ResponseReader responseReader;
	private final ObjectSerializer objectSerializer;
	private final ObjectDeserializer objectDeserializer;
	
	DefaultClientConfig() {
		this.requestWriter = new RequestConverter();
		this.responseReader = new ResponseConverter();
		ObjectConverter converter = new ObjectConverter();
		this.objectSerializer = converter;
		this.objectDeserializer = converter;
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
