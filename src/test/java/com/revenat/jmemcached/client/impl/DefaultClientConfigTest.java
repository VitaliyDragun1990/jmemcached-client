package com.revenat.jmemcached.client.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.revenat.jmemcached.client.ClientConfig;
import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;

public class DefaultClientConfigTest {
	private ClientConfig config;
	
	@Before
	public void setUp() {
		config = new DefaultClientConfig();
	}
	
	@Test
	public void shouldAllowToGetRequestWriter() throws Exception {
		assertNotNull("RequestWriter should not be null", config.getRequestWriter());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfRequestWriterForEveryCall() throws Exception {
		RequestWriter writerA = config.getRequestWriter();
		RequestWriter writerB = config.getRequestWriter();
		
		assertThat(writerA, sameInstance(writerB));
	}
	
	@Test
	public void shouldAllowToGetResponseReader() throws Exception {
		assertNotNull("ResponseReader should not be null", config.getResponseReader());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfResponseReaderForEveryCall() throws Exception {
		ResponseReader readerA = config.getResponseReader();
		ResponseReader readerB = config.getResponseReader();
		
		assertThat(readerA, sameInstance(readerB));
	}
	
	@Test
	public void shouldAllowToGetObjectSerializer() throws Exception {
		assertNotNull("ObjectSerializer should not be null", config.getObjectSerializer());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfObjectSerializerForEveryCall() throws Exception {
		ObjectSerializer serializerA = config.getObjectSerializer();
		ObjectSerializer serializerB = config.getObjectSerializer();
		
		assertThat(serializerA, sameInstance(serializerB));
	}
	
	@Test
	public void shouldAllowToGetObjectDeserializer() throws Exception {
		assertNotNull("ObjectDeserializer shold not be null", config.getObjectDeserializer());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfObjectDeserializerForEveryCall() throws Exception {
		ObjectDeserializer deserializerA = config.getObjectDeserializer();
		ObjectDeserializer deserializerB = config.getObjectDeserializer();
		
		assertThat(deserializerA, sameInstance(deserializerB));
	}

}
