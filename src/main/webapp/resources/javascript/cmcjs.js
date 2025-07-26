function myFunction() 
{
  var x = document.getElementById("password");
  
  if (x.type === "password") 
  {
    x.type = "text";
  } 
  else 
  {
    x.type = "password";
  }
  
}

window.onload = function() 
{
	if (document.getElementById('hiddenForm:link'))
    {
		document.getElementById('hiddenForm:link').onclick();
	} 
}	
