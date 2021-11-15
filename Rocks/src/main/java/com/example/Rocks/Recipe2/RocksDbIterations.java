package com.example.Rocks.Recipe2;

import com.example.Rocks.Recipe1.KeyValueStoreImpl;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;

@Slf4j
public class RocksDbIterations extends KeyValueStoreImpl {

    public void getAllKeys(RocksDB db) {
        RocksIterator itr = db.newIterator(); // Get the iterator object
        /*
           Seek to first will start iterating from the first record of Db.
           Similarly you can use seekToLast() to iterate from last record or iterate in reverse direction.
         */
        itr.seekToFirst();
        log.info("Iterating all keys..");
        while(itr.isValid()) {
            String key = new String(itr.key());
            String value = new String(itr.value());
            log.info("Iterating at key {} and value {}", key , value);
            itr.next();
        }
        itr.close();
    }

    public void getKeysByPrefix(RocksDB db, String prefix) {
        RocksIterator itr = db.newIterator();

        itr.seek(prefix.getBytes());
        log.info("Iterating from key {}", prefix);
        while(itr.isValid()) {
            String key = new String(itr.key());
            String value = new String(itr.value());
            if(!key.startsWith(prefix))
                break;
            log.info("Iterating at key {} and value {}", key , value);
            itr.next();
        }
        itr.close();

    }

    public void iterateKeysFromStartPoint(RocksDB db, String startingKey) {
        RocksIterator itr = db.newIterator();
        /*
        This is use to start the iterator from a particular key. This is a prefix match.
         */
        itr.seek(startingKey.getBytes());
        log.info("Iterating from key {}", startingKey);
        while(itr.isValid()) {
            String key = new String(itr.key());
            String value = new String(itr.value());
            log.info("Iterating at key {} and value {}", key , value);
            itr.next();
        }
        itr.close();
    }

    public void seekForPrev(RocksDB db, String target) {
        RocksIterator itr = db.newIterator();
        itr.seekForPrev(target.getBytes());
        if(itr.isValid()){
            String key = new String(itr.key());
            String value = new String(itr.value());
            log.info("Seeking at key {} and value {}", key , value);
        }
        itr.close();
    }

    public void tailingIterator(RocksDB db) throws InterruptedException {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    log.info("Putting " + i);
                    db.put(("key " + i).getBytes(), ("value " + i).getBytes());
                    Thread.sleep(100);
                } catch (InterruptedException | RocksDBException e) {
                    e.printStackTrace();
                }
            }
        }, "Putting thread");
        t.start();

        Thread.sleep(100); // wait for sometime

        ReadOptions readOptions = new ReadOptions();
        readOptions.setTailing(true);
        try (RocksIterator rocksIterator = db.newIterator(readOptions)) {
            for (rocksIterator.seekToFirst(); rocksIterator.isValid(); rocksIterator.next()) {
                log.info(new String(rocksIterator.key()) + " = " + new String(rocksIterator.value()));
            }
        }
        t.join();
    }
}
