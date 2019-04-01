package com.revenat.jmemcached.client.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;

public class DefaultClientContextTest {
	private ClientContext context;
	
	@Before
	public void setUp() {
		context = new DefaultClientContext();
	}
	
	@Test
	public void shouldAllowToGetRequestWriter() throws Exception {
		assertNotNull("RequestWriter should not be null", context.getRequestWriter());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfRequestWriterForEveryCall() throws Exception {
		RequestWriter writerA = context.getRequestWriter();
		RequestWriter writerB = context.getRequestWriter();
		
		assertThat(writerA, sameInstance(writerB));
	}
	
	@Test
	public void shouldAllowToGetResponseReader() throws Exception {
		assertNotNull("ResponseReader should not be null", context.getResponseReader());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfResponseReaderForEveryCall() throws Exception {
		ResponseReader readerA = context.getResponseReader();
		ResponseReader readerB = context.getResponseReader();
		
		assertThat(readerA, sameInstance(readerB));
	}
	
	@Test
	public void shouldAllowToGetObjectSerializer() throws Exception {
		assertNotNull("ObjectSerializer should not be null", context.getObjectSerializer());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfObjectSerializerForEveryCall() throws Exception {
		ObjectSerializer serializerA = context.getObjectSerializer();
		ObjectSerializer serializerB = context.getObjectSerializer();
		
		assertThat(serializerA, sameInstance(serializerB));
	}
	
	@Test
	public void shouldAllowToGetObjectDeserializer() throws Exception {
		assertNotNull("ObjectDeserializer shold not be null", context.getObjectDeserializer());
	}
	
	@Test
	public void shouldAllowToGetSameInstanceOfObjectDeserializerForEveryCall() throws Exception {
		ObjectDeserializer deserializerA = context.getObjectDeserializer();
		ObjectDeserializer deserializerB = context.getObjectDeserializer();
		
		assertThat(deserializerA, sameInstance(deserializerB));
	}

}
