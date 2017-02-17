var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
	
    var socket = new SockJS('/gs-guide-websocket');
    
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
        
    	setConnected(true);
        
        console.log('Connected: ' + frame);
        
        stompClient.subscribe('/topic/fileWatcher', function (fileObject) {
        	
            updateFileList(JSON.parse(fileObject.body));
            
        });
        
    });
}

function disconnect() {
	
    if (stompClient != null) {
    	
        stompClient.disconnect();
        
    }
    
    setConnected(false);
    
    console.log("Disconnected");
    
}

function testSend() {
    stompClient.send("/app/fileAdded", {}, JSON.stringify({'name': $("#name").val()}));
}

function updateFileList(file) {
	
	//Update File List Div with new File Divs

	console.log("Recieved Update: "+file);
	
	var fileRow     = document.createElement( "div" );
	$(fileRow).addClass("fileRow");
	
	var fileNameCol = document.createElement( "div" );
	$(fileNameCol).addClass("fileNameColumn");
	
	var fileLink    = document.createElement( "a" );
	$(fileLink).addClass("fileLink");
	
	var fileDateCol = document.createElement( "div" )
	$(fileDateCol).addClass("fileDateColumn");
	$(fileDateCol).text( file.date );
	
	
	var fileSizeCol = document.createElement( "div" )
	$(fileSizeCol).addClass("fileSizeColumn");
	$(fileSizeCol).addClass(" fileSizeCell");
	$(fileSizeCol).text(file.size);
	
	var fileTypeCol = document.createElement( "div" )
	$(fileTypeCol).addClass("fileTypeColumn fileSizeCell");
	$(fileTypeCol).text(file.type);
	
	// <img alt="type" class="typeIcon" src="/images/pdf.png">
	var fileIcon = document.createElement( "img" );
	$(fileIcon).addClass("typeIcon");
	$(fileIcon).attr('alt',"type");
	$(fileIcon).attr('src',file.icon);
	
	
	$(fileLink).attr('href', "/getFile?group=PRODUCT1&file="+file.name);
	$(fileLink).text(file.name);
	
	$(fileNameCol).append(fileLink);
	$(fileRow).append(fileNameCol,fileDateCol, fileSizeCol, fileTypeCol, fileIcon );
	
	$( fileRow ).addClass( "newrowAnime" );
	$( fileRow ).one( 'webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend',
			function( event ){
		console.log("Removing Animation now");
		$( fileRow ).removeClass( "newrowAnime" );
	});
	
    $( ".dayfileRows:first" ).prepend($(fileRow));
    
    console.log("Prepended row for File: "+file.name+" with TS: "+file.date);
    
}

