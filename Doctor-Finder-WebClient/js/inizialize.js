 
  $(document).ready( function () {
    // Initialize collpse button for side menu navigation on mobile devices
    $(".button-collapse").sideNav();
    
    // Initialize tabs
   // $('ul.tabs').tabs();
    
 
    
    //checkLogin();
    
   query();
});
  
  function query() {
      var GameScore = Parse.Object.extend("Doctor");
    var query = new Parse.Query(GameScore);
    //query.equalTo("playerName", "Dan Stemkoski");
    query.find({
  success: function(results) {
    alert("Successfully retrieved " + results.length + "doctors");
    // Do something with the returned Parse.Object values
    for (var i = 0; i < results.length; i++) {
      var object = results[i];
      //alert(object.id + ' - ' + object.get('playerName'));
    }
  },
  error: function(error) {
   // alert("Error: " + error.code + " " + error.message);
  }
});

       
      
      
  }
  
  function createCard(Doctor){
      
  }
