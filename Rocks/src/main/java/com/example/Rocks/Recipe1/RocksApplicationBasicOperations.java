package com.example.Rocks.Recipe1;

import com.example.Rocks.AppConfig;
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
public class RocksApplicationBasicOperations {
	private final static String FILE_NAME = "rocks-db";

	private static RocksDB getDatabase() {
		File baseDir;
		RocksDB db = null;
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

		return db;
	}

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(AppConfig.class, args);
		KeyValueStoreImpl repo = context.getBean(KeyValueStoreImpl.class);
		RocksDB db = getDatabase();
		//Putting some values to database
		repo.save(db,"1", "Person1");
		repo.save(db,"2", "Person2");
		repo.save(db,"3", "Person3");

		// Getting single value from database based on key
		repo.findSingleKey(db,"2");

		repo.save(db,"4", "Person4");
		repo.save(db,"2", "Person5");

		repo.findSingleKey(db,"2");
		repo.findSingleKey(db,"4");

		//Getting multiple values from DB based on multiple keys
		repo.findMultipleKey(db, List.of("1","2"));

		//Deleting a key-value from db
		repo.delete(db,"2");

		//Performing batch operations
		repo.writeBatch(db);

		repo.findMultipleKey(db, List.of("3","4"));
		//repo.closeDb();
	}

}
