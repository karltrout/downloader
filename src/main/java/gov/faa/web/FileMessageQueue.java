package gov.faa.web;

import org.apache.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class FileMessageQueue {

	Logger logger = Logger.getLogger(getClass());

    @MessageMapping("/fileAdded")
    @SendTo("/topic/fileWatcher")
    public String greeting(String message) throws Exception {
       // Thread.sleep(1000); // simulated delay
    	
    	logger.info("RECIEVED MESSAGE: "+message);
        return message;
    }

}
