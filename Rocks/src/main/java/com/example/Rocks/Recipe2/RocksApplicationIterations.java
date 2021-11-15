package com.example.Rocks.Recipe2;

import com.example.Rocks.AppConfig;
import com.example.Rocks.Recipe1.KeyValueStoreImpl;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@SpringBootApplication
@Slf4j
public class RocksApplicationIterations {
	private final static String FILE_NAME = "rocks-db-iter";

	private static RocksDB getDatabase() {
		File baseDir;
		RocksDB db = null;
		RocksDB.loadLibrary();
		/*
		Configure a bloom filter
		 */
		BlockBasedTableConfig tableConfig = new BlockBasedTableConfig();
		tableConfig.setFilterPolicy(new BloomFilter(10, false));

		final Options options = new Options();
        /*
         Configure as many options as per your requirements here before opening of db.
         */
		options.setCreateIfMissing(true);
		options.setCreateMissingColumnFamilies(true);
		options.setTableFormatConfig(tableConfig);
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

		return db;
	}

	public static void main(String[] args) throws InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(AppConfig.class, args);
		RocksDbIterations repo = new RocksDbIterations();
		RocksDB db = getDatabase();
		repo.save(db,"Batch1/1", "Person1");
		repo.save(db,"Batch1/2", "Person2");
		repo.save(db,"Batch1/3", "Person3");
		repo.save(db,"Batch2/4", "Person4");
		repo.save(db,"Batch2/5", "Person5");
		repo.save(db,"Batch3/1", "Person6");


		repo.getAllKeys(db);
		repo.iterateKeysFromStartPoint(db, "Batch2");
		repo.getKeysByPrefix(db, "Batch2");
		repo.seekForPrev(db, "Batch2/6");
		repo.tailingIterator(db);
	}

}
