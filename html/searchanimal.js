function createNode(element) {
  return document.createElement(element);
}

function append(parent, el) {
return parent.appendChild(el);
}

document.getElementById('submit').onclick = function() {
const ul = document.getElementById('animals');

let form = document.getElementById('search');
let name = form.elements['name'].value;
let type = form.elements['type'].value;
let breed = form.elements['breed'].value;
let color = form.elements['color'].value;
let sex = form.elements['sex'].value;  
ul.innerHTML = '';

const url = `../api/get/animals?name=${name}&type=${type}&breed=${breed}&color=${color}&sex=${sex}`;
fetch(url)
  .then((resp) => resp.json())
    .then(function(data) {
      return data.map(function(data) {
        let li = createNode('li'),
        span = createNode('span');
        span.innerHTML = `${data.Name} ${data.Breed} ${data.Type}`;
        append(li, span);
        append(ul, li);
      })
    })
    .catch(function(error) {
    console.log(error);
  });
};