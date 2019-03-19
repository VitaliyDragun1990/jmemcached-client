package com.revenat.jmemcached.client.impl;

import java.io.IOException;

import com.revenat.jmemcached.client.Client;

/**
 * Factory which are responsible for building {@link Client} instances.
 * 
 * @author Vitaly Dragun
 *
 */
public final class JMemcachedClientFactory {
	public static final int DEFAULT_PORT = 9010;
	public static final String DEFAULT_HOST = "localhost";

	/**
	 * Builds new {@link Client} instance connected to {@code JMemcached} server
	 * using provided {@code host} and {@code port} parameters.
	 * 
	 * @param host host of the {@code JMemcached} server
	 * @param port port of the {@code JMemcached} server
	 * @return configured {@link Client} instance connected to {@code JMemcached}
	 *         server
	 * @throws IOException
	 */
	public static Client buildNewClient(String host, int port) throws IOException {
		return null;
	}

	/**
	 * Builds new {@link Client} instance connected to {@code JMemcached} server
	 * using provided {@code host} parameter. Server's port value would be {@value #DEFAULT_PORT}
	 * 
	 * @param host host of the {@code JMemcached} server
	 * @return configured {@link Client} instance connected to {@code JMemcached}
	 *         server
	 * @throws IOException
	 */
	public static Client buildNewClient(String host) throws IOException {
		return buildNewClient(host, DEFAULT_PORT);
	}

	/**
	 * Builds new {@link Client} instance connected to {@code JMemcached} server.
	 * Server's host and port value would be {@value #DEFAULT_HOST} and {@value #DEFAULT_PORT}
	 * respectively.
	 * 
	 * @param host host of the {@code JMemcached} server
	 * @param port port of the {@code JMemcached} server
	 * @return configured {@link Client} instance connected to {@code JMemcached}
	 *         server
	 * @throws IOException
	 */
	public static Client buildNewClient() throws IOException {
		return buildNewClient(DEFAULT_HOST, DEFAULT_PORT);
	}
	
	private JMemcachedClientFactory() {}
}
