$(document).ready(function(){
  $("button[name='submitButton']").click(function(){

	var firstName = $("input[name='firstName']").val();
    var lastName = $("input[name='lastName']").val();
    var age = $("input[name='age']").val();
    var countryName = $("div.country__cmp").attr("country");

      $.ajax({
          url: "/bin/saveUserDetails",
          type: "get",
          data: { 
            firstName: firstName, 
            lastName: lastName, 
            coutryName: countryName,
              age: age
          },
          success: function(response) {
            alert("Form Submitted Successfully and node is creaed under '/var/anf-code-challenge' ");
          },
          error: function(xhr) {
           alert("Please enter Age between 18 to 50");
          }
        });

  });
});