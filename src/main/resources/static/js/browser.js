// --------------------------------------------------------------------------------------
// -- Assign the headers sort functions
// --------------------------------------------------------------------------------------

$(".fileNameSort").click( function(){
	$( this ).parents("div.day").find( "div.fileNameArrow" ).toggleClass( "arrow-up" );
	$( this ).parents("div.day").find( "div.fileNameArrow" ).toggleClass( "arrow-down" );
	$( this ).toggleClass( "assending" );
	
	sortRows($( this ).parents("div.day").find("div.dayfileRows"), ".fileNameColumn",  $( this ).hasClass( "assending" ));
	
});

$(".modifiedSort").click( function(){
	$( this ).parents("div.day").find( "div.modifiedArrow" ).toggleClass( "arrow-up" );
	$( this ).parents("div.day").find( "div.modifiedArrow" ).toggleClass( "arrow-down" );
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