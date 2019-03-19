package com.revenat.jmemcached.client;

import java.util.concurrent.TimeUnit;

import com.revenat.jmemcached.client.impl.JMemcachedClientFactory;

public class ClientFunctionalityTest {

	public static void main(String[] args) throws Exception {
		// Connects to JMemcached server which is listening on port 9010 at 'localhost'
		try (Client client = JMemcachedClientFactory.buildNewClient("localhost", 9010)) {
			client.put("test", "Hello world");
			System.out.println(client.get("test").get());
			
			client.remove("test");
			System.out.println(client.get("test").orElse(null));
			
			client.put("test", "Hello world");
			client.put("test", new BusinessObject("TEST"));
			
			System.out.println(client.get("test").get());
			client.clear();
			System.out.println(client.get("test").orElse(null));
			
			client.put("devstudy", "Devstudy JMemcached", 2, TimeUnit.SECONDS);
			TimeUnit.SECONDS.sleep(3);
			System.out.println(client.get("devstudy").orElse(null));
		}
	}
	
	public static class BusinessObject {
		private String message;

		public BusinessObject(String string) {
			message = string;
		}

		@Override
		public String toString() {
			return String.format("BusinessObject [message=%s]", message);
		}
		
	}
}
