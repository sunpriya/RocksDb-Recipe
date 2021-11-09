package com.example.Rocks.Recipe1;

import org.rocksdb.Status;

import java.util.Optional;

public interface KeyValueStore<K,V> {
    /**
     * Save takes in a key and a value of any type which is to be stored in Rocks
     * @param key
     * @param value
     * @return
     */
    boolean save(K key, V value);

    /**
     * Find method is used to return a value from Rocks DB corresponding to a key if present.
     * If the value is not present, returns an empty optional.
     * @param key
     * @return
     */
    Optional<V> find(K key);

    /**
     * Delete method is used to delete a record from Rocks corresponding to a given key.
     * @param key
     * @return
     */
    boolean delete(K key);

    /**
     * This is used to close the database after performing all the operations.
     * @return Status object after closing the DB
     */
    void closeDb();
}
