 
  $(document).ready( function () {
    // Initialize collpse button for side menu navigation on mobile devices
    $(".button-collapse").sideNav();
    
    // Initialize tabs
   // $('ul.tabs').tabs();
    
 
    var result="";
    result+=" <div class=\"container\">\
    <div class=\"section\"> \
    <div class=\"card-panel\">\
    <span class=\"blue-text text-darken-2\">Ciao!\
    </span> ";
     result+="</div></div>\
     </div>";
      var html=document.getElementById("card-view");
      //html.innerHTML=result;
  //console.log(html);
      
          var  map_container= document.getElementById("map_container");
      
      map_container.style.display = "none";
      
      
 
  
  // query(html);
   //console.log(query());
   
   
   var  mappa= document.getElementById("mappa");

    mappa.addEventListener('click', function() {
        
         //var html=document.getElementById("card-view");
        // createMap(html);
     map_container= document.getElementById("map_container");
      
      map_container.style.display = "block";
   
       
   
    }, false);

   var  list= document.getElementById("lista");

    list.addEventListener('click', function() {
        
         var html=document.getElementById("card-view");
         
   // alert('Hello world');
    }, false);
   
});
  var map;
      function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: -34.397, lng: 150.644},
          zoom: 8
        });
      }
  
  function query(inner) {
      var Doctor = Parse.Object.extend("Doctor");
    var query = new Parse.Query(Doctor);
     var html="";
     html+=" <div class=\"container\">\
    <div class=\"section\"> \
    ";
    //query.equalTo("playerName", "Dan Stemkoski");
    query.find({
  success: function(results) {
    //alert("Successfully retrieved " + results.length + "doctors");
    // Do something with the returned Parse.Object values
   
    for (var i = 0; i < results.length; i++) {
      var object = results[i];
      //html+="i";
      //console.log(html);
      html+=createCard(object);
      //alert(object.id + \" - \" + object.get(\"playerName'));
      
      
    }
    
     html+="</div>\</div>";
    //console.log(html);
    inner.innerHTML=html;
   // console.log(inner);
 
    
  },
  error: function(error) {
    alert("Error: " + error.code + " " + error.message);
  }
  
});
   
      
      
  }
  
  
  
  
  function createCard(Doctor){
      var Name= Doctor.get("FirstName");
      Name+=" ";
      Name+= Doctor.get("LastName");
      var Spec=Doctor.get("Specialization");
      var result="";
      result+="\
    <div class=\"card-panel\">\
    <span class=\"blue-text text-darken-2\">\
"+Name+"</span>";
    result+="\</div>";
      
      
     // document.getElementById('card-view').innerHTML =Name;
      //console.log(result);
      return result;
      
      
  }
