package gov.faa.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import gov.faa.web.properties.FileStoreProperties;

@Controller
public class FileBrowserController {

	@Autowired 
		FileStoreProperties fileStoreProperties;

	Logger logger = Logger.getLogger(getClass());
	

	 
	
	
	@GetMapping("/fileBrowser/{groupId}")
    public String browseFiles(@PathVariable String groupId, Model model) {
		
		String rootFilePath = fileStoreProperties.getLocation();
		
		logger.info("Property value: "+fileStoreProperties.getLocation());
    	logger.info("Asked for Group: "+groupId);
    	
		Path rootLocation = Paths.get(rootFilePath +groupId);
		
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
			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(file.lastModified());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

			Date fileDay = c.getTime();
			if( days.get(fileDay) != null)
				days.get(fileDay).add(new FileObject(file));
			
			else{
				ArrayList<FileObject> newList = new ArrayList<>();
				newList.add(new FileObject(file));
				days.put(fileDay, newList);
			}
			
		}
		
		for(Date day : days.keySet()){
			Collections.sort(days.get(day), new Comparator<FileObject>() {

				@Override
				public int compare(FileObject o1, FileObject o2) {
					return o2.getDate().compareTo(o1.getDate());
				}
				
			} );
		}
    	
    	model.addAttribute("groupId", groupId);
    	model.addAttribute("days", days);
    	
    	return "filebrowser";

    }
	
	@GetMapping("/getFile")
    public void getFile(HttpServletResponse response,@RequestParam("file") String name, @RequestParam("group") String groupId, Model model) {
		{
			String rootFilePath = fileStoreProperties.getLocation();
			Path rootLocation = Paths.get(rootFilePath +groupId);
	        File file = rootLocation.resolve(name).toFile();
	        
	        logger.info("Attempting to return file: "+file.getPath());
	        
	        if(!file.exists()){
	            String errorMessage = "Sorry. The file you are looking for does not exist";
	            System.out.println(errorMessage);
	            
	            OutputStream outputStream = null;
				try {
					outputStream = response.getOutputStream();
				
		            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));

		            outputStream.close();
		            
	            } catch (IOException e) {
					e.printStackTrace();
				}

	            return;
	        }
	         
	        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
	        if(mimeType==null){
	            System.out.println("mimetype is not detectable, will take default");
	            mimeType = "application/octet-stream";
	        }
	         
	        logger.info("mimetype : "+mimeType);
	         
	        response.setContentType(mimeType);
	         
	        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
	            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
	        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
	 
	         
	        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
	        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
	         
	        response.setContentLength((int)file.length());
	 
	        InputStream inputStream;
			try {
				
				inputStream = new BufferedInputStream(new FileInputStream(file));
		        //Copy bytes from source to destination(outputstream in this example), closes both streams.			
		        FileCopyUtils.copy(inputStream, response.getOutputStream());	
		        
			} catch ( IOException e) {
				
				e.printStackTrace();
				
			} 
	 
		}
	}
    
}
