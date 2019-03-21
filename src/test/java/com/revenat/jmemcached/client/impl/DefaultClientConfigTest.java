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
	public void returnsRequestWriter() throws Exception {
		assertNotNull("RequestWriter should not be null", config.getRequestWriter());
	}
	
	@Test
	public void returnsTheSameInstanceOfTheRequestWriterForEveryCall() throws Exception {
		RequestWriter writerA = config.getRequestWriter();
		RequestWriter writerB = config.getRequestWriter();
		
		assertThat(writerA, sameInstance(writerB));
	}
	
	@Test
	public void returnsResponseReader() throws Exception {
		assertNotNull("ResponseReader should not be null", config.getResponseReader());
	}
	
	@Test
	public void returnsTheSameInstanceOfTheResponseReaderForEveryCall() throws Exception {
		ResponseReader readerA = config.getResponseReader();
		ResponseReader readerB = config.getResponseReader();
		
		assertThat(readerA, sameInstance(readerB));
	}
	
	@Test
	public void returnsObjectSerializer() throws Exception {
		assertNotNull("ObjectSerializer should not be null", config.getObjectSerializer());
	}
	
	@Test
	public void returnsTheSameInstanceOfObjectSerializerForEveryCall() throws Exception {
		ObjectSerializer serializerA = config.getObjectSerializer();
		ObjectSerializer serializerB = config.getObjectSerializer();
		
		assertThat(serializerA, sameInstance(serializerB));
	}
	
	@Test
	public void returnsObjectDeserializer() throws Exception {
		assertNotNull("ObjectDeserializer shold not be null", config.getObjectDeserializer());
	}
	
	@Test
	public void returnsTheSameInstanceOfObjectDeserializerForEveryCall() throws Exception {
		ObjectDeserializer deserializerA = config.getObjectDeserializer();
		ObjectDeserializer deserializerB = config.getObjectDeserializer();
		
		assertThat(deserializerA, sameInstance(deserializerB));
	}

}
