package com.revenat.jmemcached.client;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.jmemcached.client.impl.JMemcachedClientFactory;
import com.revenat.jmemcached.protocol.model.Status;
import com.revenat.jmemcached.server.domain.Server;
import com.revenat.jmemcached.server.domain.ServerFactory;
import com.revenat.jmemcached.server.domain.impl.JMemcachedServerFactory;

public class ClientIntegrationTest {
	private static final ServerFactory SERVER_FACTORY = new JMemcachedServerFactory();
	private static final ClientFactory CLIENT_FACTORY = new JMemcachedClientFactory();
	
	private Server server;
	private Client client;
	
	@Before
	public void setUp() throws IOException {
		int serverPort = getRandomPort();
		server = getServerOnPort(serverPort);
		server.start();
		client = CLIENT_FACTORY.buildNewClient("localhost", serverPort);
	}
	
	@After
	public void tearDown() {
		server.stop();
	}
	
	@Test
	public void shouldAllowToPutDataIntoTheStore() throws Exception {
		String data = "test";
		String key = "key";
		client.put(key, data);
		
		Optional<Serializable> retrievedData = client.get(key);
		assertTrue("Retrieved data should be present", retrievedData.isPresent());
	}
	
	@Test
	public void shouldAllowToRetrieveDataFromTheStore() throws Exception {
		BusinessObject data = new BusinessObject("message");
		String key = "key";
		client.put(key, data);
		
		Optional<BusinessObject> optional = client.get(key);
		assertThat(optional.get(), equalTo(data));
	}
	
	@Test
	public void shouldAllowToRemoveDataFromTheStore() throws Exception {
		BusinessObject data = new BusinessObject("message");
		String key = "key";
		client.put(key, data);
		
		client.remove(key);
		
		Optional<BusinessObject> retrievedData = client.get(key);
		assertFalse("retrieved data should be empty after remove", retrievedData.isPresent());
	}
	
	@Test
	public void shouldReturnStatusRemovedIfRemoveWasSuccessful() throws Exception {
		BusinessObject data = new BusinessObject("message");
		String key = "key";
		client.put(key, data);
		
		Status status = client.remove(key);
		
		assertThat(status,equalTo(Status.REMOVED));
	}
	
	@Test
	public void shouldReturnStatusNotFoundIfRemoveFailed() throws Exception {
		String key = "key";
		
		Status status = client.remove(key);
		
		assertThat(status,equalTo(Status.NOT_FOUND));
	}
	
	@Test
	public void shouldAllowToClearTheStore() throws Exception {
		String keyA = "key";
		BusinessObject dataA = new BusinessObject("message");
		String keyB = "another key";
		Integer dataB = 125;
		client.put(keyA, dataA);
		client.put(keyB, dataB);
		
		client.clear();
		
		assertThat(client.get(keyA).isPresent(), is(false));
		assertThat(client.get(keyB).isPresent(), is(false));
	}
	
	@Test
	public void shouldReturnStatusClearedIfStoreWasCleared() throws Exception {
		String key = "key";
		BusinessObject data = new BusinessObject("message");
		client.put(key, data);
		
		Status status = client.clear();
		
		assertThat(status, equalTo(Status.CLEARED));
	}
	
	@Test
	public void shouldAllowToReplaceDataInStoreWithTheSameKey() throws Exception {
		String key = "key";
		BusinessObject dataA = new BusinessObject("message");
		Integer dataB = 125;
		
		client.put(key, dataA);
		client.put(key, dataB);
		
		Optional<Integer> retrievedData = client.get(key);
		assertThat(retrievedData.get(), equalTo(dataB));
	}
	
	@Test
	public void shouldReturnStatusAddedIfDataWasAddedToTheStore() throws Exception {
		String key = "key";
		BusinessObject data = new BusinessObject("message");
		
		Status status = client.put(key, data);
		
		assertThat(status, equalTo(Status.ADDED));
	}
	
	@Test
	public void shouldReturnStatusReplacedIfDataWasReplacedAtTheStore() throws Exception {
		String key = "key";
		BusinessObject dataA = new BusinessObject("message");
		Integer dataB = 125;
		
		client.put(key, dataA);
		Status status = client.put(key, dataB);
		
		assertThat(status, equalTo(Status.REPLACED));
	}
	
	@Test
	public void shouldAllowToAddDataToTheStoreWithTimeToLive() throws Exception {
		String key = "key";
		BusinessObject data = new BusinessObject("message");
		int timeToLive = 5;
		
		Status status = client.put(key, data, timeToLive, TimeUnit.SECONDS);
		
		assertThat(status, equalTo(Status.ADDED));
	}
	
	@Test
	public void shouldAllowToRetrieveDataWithTimeToLeaveIfItIsNotExpired() throws Exception {
		String key = "key";
		BusinessObject data = new BusinessObject("message");
		int timeToLive = 5;
		
		client.put(key, data, timeToLive, TimeUnit.SECONDS);
		
		assertThat(client.get(key).get(), equalTo(data));
	}
	
	@Test
	public void shouldReturnNoDataIfDataIsExpired() throws Exception {
		String key = "key";
		BusinessObject data = new BusinessObject("message");
		int timeToLive = 1;
		
		client.put(key, data, timeToLive, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(timeToLive*2);
		
		assertThat(client.get(key).isPresent(), is(false));
	}

	private Server getServerOnPort(int serverPort) {
		Properties props = new Properties();
		props.setProperty("jmemcached.server.port", Integer.toString(serverPort));
		return SERVER_FACTORY.buildNewServer(props);
	}
	
	private static int getRandomPort() {
		return ThreadLocalRandom.current().nextInt(9000, 10000);
	}
	
	public static class BusinessObject implements Serializable {
		private static final long serialVersionUID = -7004501900870324844L;
		
		private String message;

		public BusinessObject(String string) {
			message = string;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(message);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BusinessObject other = (BusinessObject) obj;
			return Objects.equals(message, other.message);
		}

		@Override
		public String toString() {
			return String.format("BusinessObject [message=%s]", message);
		}
		
	}
}
