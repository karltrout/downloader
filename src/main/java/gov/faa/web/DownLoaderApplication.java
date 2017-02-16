package gov.faa.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

import gov.faa.web.properties.FileStoreProperties;


@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties({FileStoreProperties.class})
public class DownLoaderApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(DownLoaderApplication.class, args);
	}
}
