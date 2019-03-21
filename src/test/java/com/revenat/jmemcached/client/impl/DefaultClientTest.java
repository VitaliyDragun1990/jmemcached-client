package com.revenat.jmemcached.client.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.revenat.jmemcached.client.ClientConfig;
import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.ResponseReader;
import com.revenat.jmemcached.protocol.model.Request;
import com.revenat.jmemcached.protocol.model.Response;
import com.revenat.jmemcached.protocol.model.Status;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DefaultClientTest {
	private static final Object OBJECT = new Object();
	private static final byte[] DATA = new byte[] {0};
	private static final String KEY = "key";
	private static final ByteArrayInputStream INPUT = new ByteArrayInputStream(new byte[0]);
	private static final ByteArrayOutputStream OUTPUT = new ByteArrayOutputStream();

	@Mock
	private RequestWriter requestWriter;
	@Mock
	private ResponseReader responseReader;
	@Mock
	private ObjectSerializer serializer;
	@Mock
	private ObjectDeserializer deserializer;

	private ClientConfig clientConfigStub;

	@Spy
	private Socket socket;

	private DefaultClient client;

	@Before
	public void setUp() throws IOException {
		clientConfigStub = new ClientConfigStub();
		doReturn(INPUT).when(socket).getInputStream();
		doReturn(OUTPUT).when(socket).getOutputStream();
		client = new DefaultClient(clientConfigStub, socket);
	}

	@Test
	public void closesSocket() throws Exception {
		client.close();

		verify(socket, times(1)).close();
	}

	@Test
	public void clearsJMemcachedStore() throws Exception {
		Request expected = Request.clear();
		when(responseReader.readFrom(INPUT)).thenReturn(Response.empty(Status.CLEARED));
		assertWriteRequest(expected);

		Status result = client.clear();

		assertThat(result, equalTo(Status.CLEARED));
		verify(requestWriter, times(1)).writeTo(same(OUTPUT), equals(expected));
		verify(responseReader, times(1)).readFrom(INPUT);
	}

	@Test
	public void removesObjectFromJMemcachedStore() throws Exception {
		Request expected = Request.remove(KEY);
		assertWriteRequest(expected);
		when(responseReader.readFrom(INPUT)).thenReturn(Response.empty(Status.REMOVED));
		
		Status result = client.remove(KEY);
		
		assertThat(result, equalTo(Status.REMOVED));
		verify(requestWriter, times(1)).writeTo(same(OUTPUT), equals(expected));
		verify(responseReader, times(1)).readFrom(INPUT);
	}
	
	@Test
	public void returnsObjectFromJMemcachedStore() throws Exception {
		Request expected = Request.get(KEY);
		assertWriteRequest(expected);
		when(responseReader.readFrom(INPUT)).thenReturn(Response.withData(Status.GOTTEN, DATA));
		when(deserializer.fromByteArray(any())).thenReturn(Optional.of(OBJECT));
		
		Optional<Object> result = client.get(KEY);
		
		assertThat(result.get(), equalTo(OBJECT));
		verify(requestWriter, times(1)).writeTo(same(OUTPUT), equals(expected));
		verify(responseReader, times(1)).readFrom(INPUT);
		verify(deserializer, times(1)).fromByteArray(eq(DATA));
	}
	
	@Test
	public void putsObjectIntoJMemcachedStoreWithoutTtl() throws Exception {
		Request expected = Request.put(KEY, DATA, null);
		assertWriteRequest(expected);
		when(responseReader.readFrom(INPUT)).thenReturn(Response.empty(Status.ADDED));
		when(serializer.toByteArray(any())).thenReturn(DATA);
		
		Status result = client.put(KEY, OBJECT);
		
		assertThat(result, equalTo(Status.ADDED));
		verify(requestWriter, times(1)).writeTo(same(OUTPUT), equals(expected));
		verify(responseReader, times(1)).readFrom(INPUT);
		verify(serializer, times(1)).toByteArray(eq(OBJECT));
	}
	
	@Test
	public void putsObjectIntoJMemcachedStoreWitTtl() throws Exception {
		int ttl = 10;
		Request expected = Request.put(KEY, DATA, (long) (ttl*1000));
		assertWriteRequest(expected);
		when(responseReader.readFrom(INPUT)).thenReturn(Response.empty(Status.ADDED));
		when(serializer.toByteArray(any())).thenReturn(DATA);
		
		Status result = client.put(KEY, OBJECT, ttl, TimeUnit.SECONDS);
		
		assertThat(result, equalTo(Status.ADDED));
		verify(requestWriter, times(1)).writeTo(same(OUTPUT), equals(expected));
		verify(responseReader, times(1)).readFrom(INPUT);
		verify(serializer, times(1)).toByteArray(eq(OBJECT));
	}

	private void assertWriteRequest(Request expected) throws IOException {
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Request actual = invocation.getArgument(1);
				assertThat(actual, equalTo(expected));
				return null;
			}
		}).when(requestWriter).writeTo(any(OutputStream.class), any(Request.class));
	}
	
	private static Request equals(final Request expected) {
		return Mockito.argThat(new ArgumentMatcher<Request>() {
			@Override
			public boolean matches(Request actual) {
				return Objects.equals(expected.getCommand(), actual.getCommand()) &&
						Objects.equals(expected.getKey(), actual.getKey()) &&
						Objects.equals(expected.getTtl(), actual.getTtl()) &&
						Arrays.equals(expected.getData(), actual.getData());
			}
		});
	}

	private class ClientConfigStub implements ClientConfig {
		@Override
		public RequestWriter getRequestWriter() {
			return DefaultClientTest.this.requestWriter;
		}

		@Override
		public ResponseReader getResponseReader() {
			return DefaultClientTest.this.responseReader;
		}

		@Override
		public ObjectSerializer getObjectSerializer() {
			return DefaultClientTest.this.serializer;
		}

		@Override
		public ObjectDeserializer getObjectDeserializer() {
			return DefaultClientTest.this.deserializer;
		}
	}
}
