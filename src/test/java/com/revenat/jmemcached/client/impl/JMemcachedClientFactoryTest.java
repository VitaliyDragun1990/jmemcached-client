package com.revenat.jmemcached.client.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.jmemcached.exception.JMemcachedConfigException;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JMemcachedClientFactoryTest {

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@Mock
	private Socket socket;
	
	private JMemcachedClientFactory factory;
	
	@Before
	public void setUp() {
		factory = new TestableClientFactory(socket);
	}
	
	@Test
	public void throwsNullPointerExceptionIfBuildNewClientWithNullHost() throws Exception {
		expected.expect(NullPointerException.class);
		expected.expectMessage(containsString("host can not be null"));
		
		factory.buildNewClient(null, 0);
	}
	
	@Test
	public void throwsJMemcachedConfigExceptionIfBuildNewClientWithEmptyHost() throws Exception {
		expected.expect(JMemcachedConfigException.class);
		expected.expectMessage(containsString("host can not be empty"));
		
		factory.buildNewClient("", 0);
	}
	
	@Test
	public void throwsJMemcachedConfigExceptionIfBuildNewClientWithNegativePortValue() throws Exception {
		expected.expect(JMemcachedConfigException.class);
		expected.expectMessage(containsString(
				String.format("Invalid port number. Valid port number range is from 0 to 65535. Specified port value: %d",
						-1)));
		
		factory.buildNewClient("localhost", -1);
	}
	
	@Test
	public void throwsJMemcachedConfigExceptionIfBuildNewClientWithIllegalPortValue() throws Exception {
		expected.expect(JMemcachedConfigException.class);
		expected.expectMessage(containsString(
				String.format("Invalid port number. Valid port number range is from 0 to 65535. Specified port value: %d",
						70000)));
		
		factory.buildNewClient("localhost", 70000);
	}
	
	@Test
	public void returnsDefaultHostValue() throws Exception {
		String defaultHost = factory.getDefaultHost();
		
		assertThat(defaultHost, equalTo(JMemcachedClientFactory.DEFAULT_HOST));
	}
	
	@Test
	public void returnsDefaultPortValue() throws Exception {
		int defaultPort = factory.getDefaultPort();
		
		assertThat(defaultPort, equalTo(JMemcachedClientFactory.DEFAULT_PORT));
	}
	
	@Test
	public void returnsClientUsingProvidedHostAndPort() throws Exception {
		assertNotNull("Client should not be null", factory.buildNewClient("myhost", 9090));
	}
	
	@Test
	public void returnsClientUsingProvidedHostAndDefaultPort() throws Exception {
		assertNotNull("Client should not be null", factory.buildNewClient("myhost"));
	}
	
	@Test
	public void returnsClientUsingDefaultHostAndDefaultPort() throws Exception {
		assertNotNull("Client should not be null", factory.buildNewClient());
	}
	
	private static class TestableClientFactory extends JMemcachedClientFactory {
		private Socket socket;

		TestableClientFactory(Socket socket) {
			this.socket = socket;
		}

		@Override
		protected Socket createSocket(String host, int port) throws IOException {
			return socket;
		}
	}

}
