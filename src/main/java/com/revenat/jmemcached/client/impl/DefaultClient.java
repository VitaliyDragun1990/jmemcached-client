package com.revenat.jmemcached.client.impl;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.revenat.jmemcached.client.Client;
import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;
import com.revenat.jmemcached.protocol.model.Command;
import com.revenat.jmemcached.protocol.model.Request;
import com.revenat.jmemcached.protocol.model.Response;
import com.revenat.jmemcached.protocol.model.Status;

/**
 * Default implementation of the {@link Client} interface.
 * 
 * @author Vitaly Dragun
 *
 */
class DefaultClient implements Client {
	private final RequestWriter requestWriter;
	private final ResponseReader responseReader;
	private final ObjectSerializer objectSerializer;
	private final ObjectDeserializer objectDeserializer;

	private final Socket socket;
	private final InputStream serverInputStream;
	private final OutputStream serverOutputStream;

	DefaultClient(ClientContext context, Socket socket) throws IOException {
		this.requestWriter = context.getRequestWriter();
		this.responseReader = context.getResponseReader();
		this.objectSerializer = context.getObjectSerializer();
		this.objectDeserializer = context.getObjectDeserializer();
		this.socket = socket;
		this.serverInputStream = socket.getInputStream();
		this.serverOutputStream = socket.getOutputStream();
	}

	protected Response makeRequest(Request request) throws IOException {
		requestWriter.writeTo(serverOutputStream, request);
		return responseReader.readFrom(serverInputStream);
	}

	@Override
	public Status put(String key, Serializable object) throws IOException {
		return processPut(key, object, null, null);
	}

	@Override
	public Status put(String key, Object object, Integer ttl, TimeUnit timeUnit) throws IOException {
		return processPut(key, object, ttl, timeUnit);
	}
	
	private Status processPut(String key, Object object, Integer ttl, TimeUnit timeUnit) throws IOException {
		requireNonNull(key, "key can not be null");
		requireNonNull(object, "object can not be null");
		
		byte[] data = objectSerializer.toByteArray(object);
		Long requestTTLMillis = calculateTTL(ttl, timeUnit);
		Response response = makeRequest(
				Request.withKeyAndData(Command.PUT, key, data, requestTTLMillis));
		return response.getStatus();
	}

	private static Long calculateTTL(Integer ttl, TimeUnit timeUnit) {
		return (ttl != null && timeUnit != null) ? timeUnit.toMillis(ttl) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> Optional<T> get(String key) throws IOException {
		Response response = makeRequest(Request.withKey(Command.GET, key));
		return (Optional<T>) objectDeserializer.fromByteArray(response.getData());
	}

	@Override
	public Status remove(String key) throws IOException {
		return makeRequest(Request.withKey(Command.REMOVE, key)).getStatus();
	}

	@Override
	public Status clear() throws IOException {
		return makeRequest(Request.empty(Command.CLEAR)).getStatus();
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}
}
