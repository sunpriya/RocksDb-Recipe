package com.example.Rocks.Recipe1;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.Status;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Component
@Slf4j
public class KeyValueStoreImpl implements KeyValueStore<String, Object> {
    private final static String FILE_NAME = "rocks-db";
    File baseDir;
    RocksDB db;

    /**
     * Opening of rocks databse.
     */
    @Inject
    public  KeyValueStoreImpl () {

        RocksDB.loadLibrary();

        final Options options = new Options();
        /*
         Configure as many options as per your requirements here before opening of db.
         */
        options.setCreateIfMissing(true);
        options.setCreateMissingColumnFamilies(true);
        options.useCappedPrefixExtractor(10);
        options.allow2pc();

        baseDir = new File("/Users/Sunpriya/Desktop/work/RocksDb-Recipe/", FILE_NAME);
        try {
            Files.createDirectories(baseDir.getParentFile().toPath());
            Files.createDirectories(baseDir.getAbsoluteFile().toPath());
            log.info("Rocks path : {}", baseDir.getAbsolutePath());
            db = RocksDB.open(options, baseDir.getAbsolutePath());
            log.info("RocksDB initialized");
        } catch(RocksDBException | IOException e) {
            log.error("Error initializing RocksDB. Exception: '{}', message: '{}'", e.getCause(), e.getMessage(), e);
        }
    }

    @Override
    public synchronized boolean save(String key, Object value) {
        log.info("saving value '{}' with key '{}'", value, key);
        try {
            db.put(key.getBytes(), SerializationUtils.serialize(value));
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
    public Optional<Object> find(String key) {
        Object value = null;
        try {
            byte[] bytes = db.get(key.getBytes());
            if (bytes != null){
                value = SerializationUtils.deserialize(bytes);
            }
        } catch (RocksDBException e) {
            log.error("Status of database: {},{}", e.getStatus().getCode(), e.getStatus().getState());

            log.error(
                    "Error retrieving the entry with key: {}, cause: {}, message: {}",
                    key,
                    e.getCause(),
                    e.getMessage()
            );
        }
        log.info("finding key '{}' returns '{}'", key, value);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean delete(String key) {
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
    public void closeDb() {
        log.info("Closing the database and freeing the resources.");
        db.close();
    }
}
