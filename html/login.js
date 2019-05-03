document.getElementById("login").onclick=function(){
    sessionStorage.setItem("current_user", document.getElementById("username").value);
}