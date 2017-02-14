package gov.faa.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="files")
public class FileStoreProperties {
	/**
	 * location:
	 * this property holds the base locations for files uploaded to the server.
	 * 
	 */
	public String location; 

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
