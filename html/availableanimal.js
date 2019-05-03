function createNode(element) {
  return document.createElement(element);
}

function append(parent, el) {
  return parent.appendChild(el);
}

const ul = document.getElementById('animals');
const url = '../api/get/animals?available=1';
fetch(url)
.then((resp) => resp.json())
.then(function(data) {
  return data.map(function(data) {
    let li = createNode('li'),
    span = createNode('span');
    let a = createNode('a');
    
    span.innerHTML = `${data.Animal_ID} ${data.Name} ${data.Type} ${data.Breed}`;
    a.href = '#';
    a.onclick = function() {
      user_name = sessionStorage.getItem('current_user');
      if (user_name) {
        window.location.href = `../api/post/adopt?username=${user_name}&animal=${data.Animal_ID}`;
      }
    };
    
    append(a, span);
    append(li, a);
    append(ul, li);
  })
})
.catch(function(error) {
  console.log(error);
}); 