/*******************************************************************************
 * Copyright 2012 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package au.gov.ga.earthsci.common.collection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * {@link Map} that supports adding multiple values for a single key.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public interface CollectionMap<K, V, C extends Collection<V>> extends Map<K, C>
{
	/**
	 * Put a single key/value pair into this map.
	 * 
	 * @param key
	 * @param value
	 */
	void putSingle(K key, V value);

	/**
	 * Remove a single key/value pair from this map if it exists.
	 * 
	 * @param key
	 * @param value
	 * @return True if the key/value pair was found and removed, false
	 *         otherwise.
	 */
	boolean removeSingle(K key, V value);

	/**
	 * Return the number of entries stored for the given key
	 * 
	 * @param key
	 * 
	 * @return The number of entries stored for the given key
	 */
	int count(K key);

	/**
	 * Return a flattened view of the values in this map. This list will be
	 * ordered on the key order followed by the ordering of the collection
	 * stored at that key.
	 * 
	 * @return A flattened view of the values in this map.
	 */
	List<V> flatValues();
}
