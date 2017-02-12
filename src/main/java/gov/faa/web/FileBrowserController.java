package gov.faa.web;

import java.io.File;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileBrowserController {

	Logger logger = Logger.getLogger(getClass());
	
	@GetMapping("/fileBrowser/{groupId}")
    public String browseFiles(@PathVariable String groupId, Model model) {
		
    	logger.info("Asked for Group: "+groupId);
    	
		Path rootLocation = Paths.get("/tmp/uploadedFiles/"+groupId);
		
		File sourceDirectory = rootLocation.toFile();
		
		if (sourceDirectory.mkdirs() | sourceDirectory.exists())
		{
			logger.info("The file Storage Directory exists at: "+sourceDirectory.getAbsolutePath());
		}
		else{
			logger.error("Could not create Storage Directory :"+sourceDirectory.getAbsolutePath());
		}
		
		TreeMap<Date, ArrayList<FileObject>> days = new TreeMap<>(Collections.reverseOrder());
		
		File[] files = sourceDirectory.listFiles();
		
		for(File file: files){
			Date dateString = new Date(file.lastModified()); //DateFormat.getDateInstance().format(file.lastModified());
			
			if( days.get(dateString) != null)
				days.get(dateString).add(new FileObject(file));
			
			else{
				ArrayList<FileObject> newList = new ArrayList<>();
				newList.add(new FileObject(file));
				days.put(dateString, newList);
			}
		}
		
    	
    	model.addAttribute("groupId", groupId);
    	model.addAttribute("days", days);
    	
    	return "filebrowser";

    }
	
	class FileObject{
		private String name;
		private Date date;
		private double size;
		private String type = "unknown";
		private String icon = "/images/default.png";
		
		FileObject(File file){
			this.setName(file.getName());
			this.setDate(new Date(file.lastModified()));
			this.setSize((file.length()/1024));
			this.setType(URLConnection.guessContentTypeFromName(file.getName()));
			if (this.type == null || this.type.isEmpty()) setType("unknown");
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		private void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the date
		 */
		public Date getDate() {
			return date;
		}

		/**
		 * @param date the date to set
		 */
		private void setDate(Date date) {
			this.date = date;
		}

		/**
		 * @return the size
		 */
		public String getSize() {
			return String.format("%.0f kb", size);
		}

		/**
		 * @param size the size to set
		 */
		private void setSize(double size) {
			this.size = size;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		private void setType(String type) {
			this.type = type;
			setIcon();
		}

		private void setIcon() {
			if(this.type != null){
				switch (this.type){
				case "application/pdf":
					this.icon = "/images/pdf.png";
					break;
				case "application/rtf":
					this.icon = "/images/doc.png";
					break;
				case "text/plain":
					this.icon = "/images/doc.png";
					break;
				default:
					this.icon = "/images/zip.png";
					break;
				}
			}
		}
		
		public String getIcon(){
			return this.icon;
		}
	}
    
}
