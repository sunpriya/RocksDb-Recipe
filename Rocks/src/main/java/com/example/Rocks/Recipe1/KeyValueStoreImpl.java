package com.example.Rocks.Recipe1;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KeyValueStoreImpl implements KeyValueStore<String, Object> {



    @Override
    public synchronized boolean save(RocksDB db, String key, Object value) {
        log.info("saving value '{}' with key '{}'", value, key);
        try {
            db.put(new WriteOptions().setSync(true), key.getBytes(), SerializationUtils.serialize(value));
        } catch (RocksDBException e) {
            /*
              Status object of rocks db lets you log the status code and sub code.
             */
            log.error("Status of database: {},{}", e.getStatus().getCode(), e.getStatus().getState());
            log.error("Error saving entry. Cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Optional<Object> findSingleKey(RocksDB db, String key) {
        Object value = null;
        try {
            byte[] bytes = db.get(key.getBytes());
            if (bytes != null){
                value = SerializationUtils.deserialize(bytes);
            }
        } catch (RocksDBException e) {
            log.error("Status of database: {},{}", e.getStatus().getCode(), e.getStatus().getState());

            log.error(
                    "Error in retrieving the entry with key: {}, cause: {}, message: {}",
                    key,
                    e.getCause(),
                    e.getMessage()
            );
        }
        log.info("finding key '{}' returns '{}'", key, value);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    @Override
    public List<Object> findMultipleKey(RocksDB db, List<String> keys) {
        List<Object> retrievedList = new ArrayList<>();
        try {
            ReadOptions readOptions = new ReadOptions();
            readOptions.setVerifyChecksums(true);
            readOptions.setBackgroundPurgeOnIteratorCleanup(true);
            List<byte[]> byteStringList = keys.stream().map(String::getBytes).collect(Collectors.toList());
            retrievedList = db.multiGetAsList(readOptions, byteStringList).stream().map(it -> it!=null ? SerializationUtils.deserialize(it): "").collect(Collectors.toList());
            log.info("Values found with keys {} are {}", keys, retrievedList);
            return retrievedList;

        } catch (RocksDBException e) {
            log.error("Status of database multiget operation {}, {}", e.getStatus().getCode(), e.getStatus().getState());
            log.error(
                    "Error retrieving the entry with key: {}, cause: {}, message: {}",
                    keys,
                    e.getCause(),
                    e.getMessage()
            );
        }

        return retrievedList;
    }

    @Override
    public boolean delete(RocksDB db, String key) {
        log.info("deleting key '{}'", key);
        try {
            db.delete(key.getBytes());
        } catch (RocksDBException e) {
            log.error("Status of database: {},{}", e.getStatus().getCode(), e.getStatus().getState());
            log.error("Error deleting entry, cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void closeDb(RocksDB db) {
        log.info("Closing the database and freeing the resources.");
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void writeBatch(RocksDB db) {
        try {
            String key1 = "3";
            String key2 = "4";
            byte[] bytes = db.get(key1.getBytes());
            if(bytes != null) {
                WriteBatch batch = new WriteBatch();
                batch.delete(key1.getBytes());
                batch.put(key2.getBytes(), bytes);
                db.write(new WriteOptions(), batch);
            }
            log.info("Write batch successful");
        } catch (RocksDBException e) {
            log.error("Batch write failed with status {}, exception {}", e.getStatus().getCode(), e.getMessage());
        }

    }
}
