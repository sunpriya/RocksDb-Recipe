package com.example.Rocks;

import com.example.Rocks.Recipe1.KeyValueStoreImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class RocksApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(AppConfig.class, args);
		KeyValueStoreImpl repo = context.getBean(KeyValueStoreImpl.class);
		repo.save("1", "Person1");
		repo.save("2", "Person2");
		repo.save("3", "Person3");
		System.out.println(repo.find("2").toString());
		System.out.println(repo.find("4").toString());
		repo.save("4", "Person4");
		repo.save("2", "Person5");
		System.out.println(repo.find("2").toString());
		System.out.println(repo.find("4").toString());
		repo.delete("2");
		repo.delete("6");
		repo.closeDb();
	}

}
