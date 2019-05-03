let animalID = sessionStorage.getItem('animal_id');

updatebutton = document.getElementById("updatebutton");
updatebutton.onclick=function(){
    this.disabled = true;
    let key = document.getElementById("key").value
    let value = document.getElementById("value").value
    let post = `../api/post/editanimal?animal_id=${animalID}&key=${key}&value=${value}`;
    fetch(post);
};