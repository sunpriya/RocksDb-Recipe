package com.example.Rocks;

import com.example.Rocks.Recipe1.KeyValueStoreImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public class RocksApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(AppConfig.class, args);
		KeyValueStoreImpl repo = context.getBean(KeyValueStoreImpl.class);
		repo.save("1", "Person1");
		repo.save("2", "Person2");
		repo.save("3", "Person3");
		repo.findSingleKey("2");
		repo.save("4", "Person4");
		repo.save("2", "Person5");
		repo.findSingleKey("2");
		repo.findSingleKey("4");
		repo.findMultipleKey(List.of("1","2"));
		repo.delete("2");
		repo.writeBatch();
		repo.findMultipleKey(List.of("3","4"));
		//repo.closeDb();
	}

}
