package com.example.Rocks.Recipe1;

import org.rocksdb.RocksDB;

import java.util.List;
import java.util.Optional;

public interface KeyValueStore<K,V> {
    /**
     * Save takes in a key and a value of any type which is to be stored in Rocks
     * @param key
     * @param value
     * @return
     */
    boolean save(RocksDB db, K key, V value);

    /**
     * Find method is used to return a value from Rocks DB corresponding to a key if present.
     * If the value is not present, returns an empty optional.
     * @param key
     * @return
     */
    Optional<V> findSingleKey(RocksDB db, K key);

    /**
     * Function to get multiple keys from a column family in one go
     * @param Key
     * @return
     */
    List<V> findMultipleKey(RocksDB db, List<K> Key);

    /**
     * Delete method is used to delete a record from Rocks corresponding to a given key.
     * @param key
     * @return
     */
    boolean delete(RocksDB db, K key);

    /**
     * This is used to close the database after performing all the operations.
     * @return Status object after closing the DB
     */
    void closeDb(RocksDB db);

    /**
     * Used for atomic updates. All or none operations of the batch will be performed.
     */
    void writeBatch(RocksDB db);
}
