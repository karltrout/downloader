package gov.faa.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import gov.faa.web.files.FileWatcher;
import gov.faa.web.properties.FileStoreProperties;

@Configuration
public class appConfig {

	@Bean()
	public FileStoreProperties fileStoreProperties() {
		return new FileStoreProperties();
	}
	
	@Bean()
	public FileWatcher fileWatcher(){
		return new FileWatcher();
	}
	

}
