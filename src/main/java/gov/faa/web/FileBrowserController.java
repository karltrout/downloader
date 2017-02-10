package gov.faa.web;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
		
		HashMap<String, ArrayList<FileObject>> days = new HashMap<>();
		
		File[] files = sourceDirectory.listFiles();
		
		for(File file: files){
			String dateString = DateFormat.getDateInstance().format(file.lastModified());
			
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
		
		FileObject(File file){
			this.setName(file.getName());
			this.setDate(new Date(file.lastModified()));
			this.setSize((file.length()/1024));
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
	}
    
}
