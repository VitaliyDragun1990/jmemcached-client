package com.revenat.jmemcached.client;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.revenat.jmemcached.protocol.model.Status;

/**
 * This interface represents a client of the JMemcached Application.
 * 
 * @author Vitaly Dragun
 *
 */
public interface Client extends AutoCloseable {

	/**
	 * Puts specified {@code object} into the {@code JMemcached} store using
	 * specified {@code key} to uniquely designate it.
	 * 
	 * @param key    string that uniquely designate object inside store
	 * 
	 * @param object object to be placed inside {@code JMemcached} store
	 * @return {@link Status} object that represents result of the operation.
	 * @throws IOException
	 */
	Status put(String key, Object object) throws IOException;

	/**
	 * Puts specified {@code object} into the {@code JMemcached} store using
	 * specified {@code key} to uniquely designate it. Apply provided {@code ttl}
	 * parameter to determine how long specified object should be stored inside
	 * {@code JMemcached} store before it will be automatically deleted.
	 * 
	 * @param key      string that uniquely designate object inside
	 *                 {@code JMemcached} store
	 * @param object   object to be placed inside {@code JMemcached} store
	 * 
	 * @param ttl      number that designates time interval during which specified
	 *                 object should be stored before it will be automatically
	 *                 deleted
	 * @param timeUnit represents time unit in which {@code ttl} parameter should be
	 *                 measured
	 * 
	 * @return {@link Status} object that represents result of the operation.
	 * 
	 * @throws IOException
	 */
	Status put(String key, Object object, Integer ttl, TimeUnit timeUnit) throws IOException;

	/**
	 * Returns object that was stored using specified {@code key}.
	 * 
	 * @param key string identifier used for storing an object
	 * @return {@link Optional} with the object that was stored with specified
	 *         {@code key} if any, or the empty {@link Optional} otherwise.
	 * @throws IOException
	 */
	<T> Optional<T> get(String key) throws IOException;

	/**
	 * Removes object that was stored with the provided {@code key} from the
	 * {@code JMemcached} store.
	 * 
	 * @param key string identifier used for storing an object.
	 * @return {@link Status} object that represents result of the operation.
	 * @throws IOException
	 */
	Status remove(String key) throws IOException;

	/**
	 * Clears the {@code JMemcached} store, removing all the objects that are stored
	 * there at the time of calling this method.
	 * 
	 * @return {@link Status} object that represents result of the operation.
	 * @throws IOException
	 */
	Status clear() throws IOException;
}
