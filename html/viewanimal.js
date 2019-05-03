function createNode(element) {
  return document.createElement(element);
}

function append(parent, el) {
  return parent.appendChild(el);
}

let animalID = sessionStorage.getItem('animal_id');
const url = `../api/get/animals?animal_id=${animalID}`;
  fetch(url)
  .then((resp) => resp.json())
  .then(function(data) {
    return data.map(function(data) {
      info = document.getElementById('animal_info');
      
      namez = createNode('h1');
      namez.innerHTML = `${data.Name}`;
      append(info, namez);
      
      img = createNode('img');
      img.src = `${data.Picture_URL}`;
      append(info, img);

      desc = createNode('h3');
      desc.innerHTML = `${data.Description}`;
      append(info, desc);
      
      date = createNode('p');
      date.innerHTML = `${data.Posted_Date}`;
      append(info, date);
      
      ul = createNode('ul');
      
      li_age = createNode('li');
      age = createNode('p');
      age.innerHTML = `${data.Age}`;
      append(li_age, age);
      append(ul, li_age);
      
      li_type = createNode('li');
      type = createNode('p');
      type.innerHTML = `${data.Type}`;
      append(li_type, type);
      append(ul, li_type);
      
      li_breed = createNode('li');
      breed = createNode('p');
      breed.innerHTML = `${data.Breed}`;
      append(li_breed, breed);
      append(ul, li_breed);
      
      li_size = createNode('li');
      size = createNode('p');
      size.innerHTML = `${data.Size}`;
      append(li_size, size);
      append(ul, li_size);
      
      li_color = createNode('li');
      color = createNode('p');
      color.innerHTML = `${data.Color}`;
      append(li_color, color);
      append(ul, li_color);
      
      li_sex = createNode('li');
      sex = createNode('p');
      sex.innerHTML = `${data.Sex}`;
      append(li_sex, sex);
      append(ul, li_sex);
      
      append(info, ul);
    })
  })
  .catch(function(error) {
    console.log(error);
  });

document.getElementById("submitinquiry").onclick=function(){
  let question = document.getElementById("inquirytext").value;
  document.getElementById("inquirytext").value = "";
  let currentUser = sessionStorage.getItem("current_user");
  let inquiry_endpoint = `../api/post/inquiry?animal_id=${animalID}&username=${currentUser}&inquiry_question=${question}`;
  fetch(inquiry_endpoint);
}

document.getElementById("submitdonate").onclick=function(){
  let amount = document.getElementById("donationamount").value;
  document.getElementById("donationamount").value = "";
  let currentUser = sessionStorage.getItem("current_user");
  let donation_endpoint = `../api/post/donation?animal_id=${animalID}&username=${currentUser}&donation_amount=${amount}`;
  fetch(donation_endpoint);
}