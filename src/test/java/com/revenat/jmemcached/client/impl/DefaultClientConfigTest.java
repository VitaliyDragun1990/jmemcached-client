package com.revenat.jmemcached.client.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revenat.jmemcached.client.ClientConfig;
import com.revenat.jmemcached.exception.JMemcachedConfigException;
import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;

public class DefaultClientConfigTest {
	private static final int PORT = 8090;

	private static final String HOST = "localhost";
	
	private ClientConfig config;
	
	@Before
	public void setUp() {
		config = new DefaultClientConfig(HOST, PORT);
	}

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@Test
	public void throwsNullPointerExceptionIfCreatedWithNullHost() throws Exception {
		expected.expect(NullPointerException.class);
		expected.expectMessage(containsString("host can not be null"));
		
		config = new DefaultClientConfig(null, 0);
	}
	
	@Test
	public void throwsJMemcachedConfigExceptionIfCreatedWithEmptyHost() throws Exception {
		expected.expect(JMemcachedConfigException.class);
		expected.expectMessage(containsString("host can not be empty"));
		
		config = new DefaultClientConfig("    ", 0);
	}
	
	@Test
	public void throwsJMemcachedConfigExceptionIfCreatedWithInvalidPort() throws Exception {
		int invalidPort = -1;
		expected.expect(JMemcachedConfigException.class);
		expected.expectMessage(containsString(
				String.format("Invalid port number. Valid port number range is from 0 to 65535. Specified port value: %d",
						invalidPort)));
		
		config = new DefaultClientConfig(HOST, invalidPort);
	}
	
	@Test
	public void returnsPortNumber() throws Exception {
		assertThat(config.getPort(), equalTo(PORT));
	}
	
	@Test
	public void returnsHost() throws Exception {
		assertThat(config.getHost(), equalTo(HOST));
	}
	
	@Test
	public void returnsTheSameInstanceOfTheRequestWriterForEveryCall() throws Exception {
		RequestWriter writerA = config.getRequestWriter();
		RequestWriter writerB = config.getRequestWriter();
		
		assertThat(writerA, sameInstance(writerB));
	}
	
	@Test
	public void returnsTheSameInstanceOfTheResponseReaderForEveryCall() throws Exception {
		ResponseReader readerA = config.getResponseReader();
		ResponseReader readerB = config.getResponseReader();
		
		assertThat(readerA, sameInstance(readerB));
	}
	
	@Test
	public void returnsTheSameInstanceOfObjectSerializerForEveryCall() throws Exception {
		ObjectSerializer serializerA = config.getObjectSerializer();
		ObjectSerializer serializerB = config.getObjectSerializer();
		
		assertThat(serializerA, sameInstance(serializerB));
	}
	
	@Test
	public void returnsTheSameInstanceOfObjectDeserializerForEveryCall() throws Exception {
		ObjectDeserializer deserializerA = config.getObjectDeserializer();
		ObjectDeserializer deserializerB = config.getObjectDeserializer();
		
		assertThat(deserializerA, sameInstance(deserializerB));
	}

}
