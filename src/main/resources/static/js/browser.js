// --------------------------------------------------------------------------------------
// -- Assign the Login function
// --------------------------------------------------------------------------------------
$( "#login" ).click( function(){
	
	//alert ("gonna try and login?");
	$( "#messageBox" ).show();
	})

$( "#cancelLogin" ).click(function (){
	$( "#messageBox" ).hide();
});


// --------------------------------------------------------------------------------------
// -- Assign the headers sort functions
// --------------------------------------------------------------------------------------

$(".fileNameSort").click( function(){
	assc = $( this ).hasClass( "assending" );
	$( this ).parents("div.day").find( "div.fileNameArrowUp" ).toggleClass( "arrow-up", assc ).css("border-bottom-color", "#fff");
	$( this ).parents("div.day").find( "div.fileNameArrowDown" ).toggleClass( "arrow-down", !assc ).css("border-top-color", "#fff");

	$( this ).parents("div.day").find( "div.modifiedArrowUp" ).toggleClass( "arrow-up", true ).css("border-bottom-color", "#25649f");
	$( this ).parents("div.day").find( "div.modifiedArrowDown" ).toggleClass( "arrow-down", true ).css("border-top-color", "#25649f");
	
	$( this ).toggleClass( "assending" );
	
	sortRows($( this ).parents("div.day").find("div.dayfileRows"), ".fileNameColumn",  $( this ).hasClass( "assending" ));
	
});

$(".modifiedSort").click( function(){
	assc = $( this ).hasClass( "assending" );
	$( this ).parents("div.day").find( "div.modifiedArrowUp" ).toggleClass( "arrow-up", assc ).css("border-bottom-color", "#fff");
	$( this ).parents("div.day").find( "div.modifiedArrowDown" ).toggleClass( "arrow-down", !assc ).css("border-top-color", "#fff");

	$( this ).parents("div.day").find( "div.fileNameArrowUp" ).toggleClass( "arrow-up", true ).css("border-bottom-color", "#25649f");
	$( this ).parents("div.day").find( "div.fileNameArrowDown" ).toggleClass( "arrow-down", true ).css("border-top-color", "#25649f");
	$( this ).toggleClass( "assending" );

	sortRows($( this ).parents("div.day").find("div.dayfileRows"), ".fileDateColumn",  $( this ).hasClass( "assending" ));
});

$.extend($.expr[":"], {
	"CIScontains": function(elem, i, match, array) {
	return (elem.textContent || elem.innerText || "").toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
	}
	});

//--------------------------------------------------------------------------------------
//-- Searching Function
//--------------------------------------------------------------------------------------

$("#searchResults").hide();

$("#textSearch").on("change paste keyup", function(){
	
	var vis = $("#searchResults").css('visibility');
	var searchterm = $( this ).val();
	
	if(searchterm === "" && vis != 'hidden'){
		$("#searchResults").hide();
	}
	else{
		$("#searchResults").show();
	}

	var foundItems = $( this ).parents("div.application").find("div.fileNameColumn").filter(':CIScontains(' + searchterm  + ')');

	$("#searchResults").empty();
	
	foundItems.each( 
			function(i, resultObj){
					ahref = $(resultObj).find("a:first");
					newResultDiv =  document.createElement( "div" );
					newLink = document.createElement( "a" );
					
					$(newLink).attr('href',$(ahref).attr("href"));
					$(newLink).text($(ahref).text());
					$(newResultDiv).append(newLink);
					
					$("#searchResults").append(newResultDiv);
				});

});


//--------------------------------------------------------------------------------------
//-- Sorting Function
//--------------------------------------------------------------------------------------


function sortRows(dayDiv, column, sortDecending) {
	
	var $divs = dayDiv.find("div.fileRow");

    var alphabeticallyOrderedDivs = $divs.sort(function (a, b) {
    	if(sortDecending){
    		
    		return $(a).find(column).text().localeCompare($(b).find(column).text());
    		
    	}else{

            return $(b).find(column).text().localeCompare($(a).find(column).text());
            
    	}
    });
    
    dayDiv.html(alphabeticallyOrderedDivs);
};

$("div.mainBody").height($( window ).height() - 98 ); //66 is the size of the header + body margin

$( document ).ready( function(){
	
	connect();
	$( "#messageBox" ).hide();
	
   /* $("#loginForm").submit(function(e){

        disconnect();
    	console.log("submitted form with "+$( "#loginName" ).val()+":"+$( "#loginPassword" ).val());
    	
    	/*$.post( "/fileBrowser/PRODUCT1", 
    			{ "username": $( "#loginName" ).val() , "password": $( "#loginPassword" ).val()}
    	).done(function( data ) { $( "body" ).html(data);});
        

    });
*/
})
