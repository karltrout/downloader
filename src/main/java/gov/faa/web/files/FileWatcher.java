package gov.faa.web.files;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import gov.faa.web.FileObject;

@Component
public class FileWatcher implements CommandLineRunner {

	private static final boolean WATCHING = false;

	private Logger logger = Logger.getLogger(getClass());
	
	private String name = "Directory Watcher";

	Path dir;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Value("${files.location}")
	String location;
	String group = "PRODUCT1";
	
	@Override
	public void run(String... args) throws Exception {

		if (!WATCHING) return;
		
		WatchService watcher = FileSystems.getDefault().newWatchService();

	
		dir = Paths.get(location+group);

		if (!Files.exists(dir)){
			Files.createDirectories(dir);
		}
		
		try {
		    WatchKey key = dir.register(watcher,
		                           ENTRY_CREATE,
		                           ENTRY_DELETE,
		                           ENTRY_MODIFY);
		} catch (IOException x) {
		    System.err.println(x);
		}
		System.out.println(getName() + " is running for Path " + dir.getFileName());

		for (;;) {
			System.out.println("Watching...");
			// wait for key to be signaled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				// This key is registered only
				// for ENTRY_CREATE events,
				// but an OVERFLOW event can
				// occur regardless if events
				// are lost or discarded.
				if (kind == OVERFLOW) {
					continue;
				}

				// The filename is the
				// context of the event.
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path filename = ev.context();

				// Verify that the new
				// file is a text file.
				Path child;
				
					// Resolve the filename against the directory.
					// If the filename is "test" and the directory is "foo",
					// the resolved name is "test/foo".
					child = dir.resolve(filename);
					
				/*	String fileType = Files.probeContentType(child);
					if (fileType != null && !Files.probeContentType(child).equals("text/plain")) {
						System.err.format("New file '%s'" + " is not a plain text file.%n", filename);
						continue;
					}
					*/

				// Email the file to the
				// specified email alias.
				System.out.format("New file Detected %s%n", filename);
				
				String filePath = child.toString();
				File newFile =  new File(filePath);
				
				while (newFile.lastModified() == 0){
					Thread.sleep(2000);
					logger.info("Sleeping on file wait : "+newFile.length());
					newFile =  new File(filePath);
				}
				
				logger.info("File: "+newFile.getName()+" LM: "+newFile.lastModified()+" SZ: "+newFile.length());
				
				FileObject fo = new FileObject( newFile );
				
				logger.info("SENDING TO: "+ "/topic/fileWatcher");
				if (kind == ENTRY_CREATE) {
					fo.setAction(kind.name());
					messagingTemplate.convertAndSend("/topic/fileWatcher", fo);
				}
				
				// Details left to reader....
			}

			// Reset the key -- this step is critical if you want to
			// receive further watch events. If the key is no longer valid,
			// the directory is inaccessible so exit the loop.
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private boolean isFileComplete(File file){

		long filesize = file.length();
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (file.length() != filesize && filesize != 0){

			filesize = file.length();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
		
	}
}
