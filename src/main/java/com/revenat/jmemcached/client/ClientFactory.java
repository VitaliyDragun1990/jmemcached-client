package com.revenat.jmemcached.client;

import java.io.IOException;

/**
 * Factory which is responsible for building {@link Client} instances.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ClientFactory {

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
	Client buildNewClient(String host, int port) throws IOException;
	
	/**
	 * Builds new {@link Client} instance connected to {@code JMemcached} server
	 * using provided {@code host} parameter. Server's port value would be {@link #getDefaultPort()}
	 * 
	 * @param host host of the {@code JMemcached} server
	 * @return configured {@link Client} instance connected to {@code JMemcached}
	 *         server
	 * @throws IOException
	 */
	Client buildNewClient(String host) throws IOException;
	
	/**
	 * Builds new {@link Client} instance connected to {@code JMemcached} server.
	 * Server's host and port value would be {@link #getDefaultHost()} and {@link #getDefaultPort() }
	 * respectively.
	 * 
	 * @param host host of the {@code JMemcached} server
	 * @param port port of the {@code JMemcached} server
	 * @return configured {@link Client} instance connected to {@code JMemcached}
	 *         server
	 * @throws IOException
	 */
	Client buildNewClient() throws IOException;
	
	/**
	 * Returns {@code JMemcached} server default host value.
	 */
	String getDefaultHost();
	
	/**
	 * Returns {@code JMemcached} server default port value.
	 */
	int getDefaultPort();
}
