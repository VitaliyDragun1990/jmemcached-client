package com.revenat.jmemcached.client.impl;

import java.io.IOException;
import java.net.Socket;

import com.revenat.jmemcached.client.Client;
import com.revenat.jmemcached.client.ClientFactory;
import com.revenat.jmemcached.exception.JMemcachedConfigException;

/**
 * Default implementation of the {@link ClientFactory}.
 * 
 * @author Vitaly Dragun
 *
 */
public class JMemcachedClientFactory implements ClientFactory {
	static final int DEFAULT_PORT = 9010;
	static final String DEFAULT_HOST = "localhost";

	@Override
	public Client buildNewClient(String host, int port) throws IOException {
		Socket socket = createSocket(validateHost(host), validatePort(port));
		return new DefaultClient(new DefaultClientContext(), socket);
	}

	protected Socket createSocket(String host, int port)
			throws IOException {
		Socket socket = new Socket(host, port);
		socket.setKeepAlive(true);
		return socket;
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
	public Client buildNewClient(String host) throws IOException {
		return buildNewClient(host, DEFAULT_PORT);
	}

	@Override
	public Client buildNewClient() throws IOException {
		return buildNewClient(DEFAULT_HOST, DEFAULT_PORT);
	}

	@Override
	public String getDefaultHost() {
		return DEFAULT_HOST;
	}

	@Override
	public int getDefaultPort() {
		return DEFAULT_PORT;
	}
}
