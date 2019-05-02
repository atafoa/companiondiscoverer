function createNode(element) {
    return document.createElement(element);
}

function append(parent, el) {
  return parent.appendChild(el);
}

const ul = document.getElementById('animals');
const url = '../api/get/animals/?=';
fetch(url)
.then((resp) => resp.json())
.then(function(data) {
  return data.map(function(data) {
    let li = createNode('li'),
    span = createNode('span');
    span2 = createNode('span');
    span.innerHTML = `${data.Name} ${data.Breed}`;
    span2.innerHTML = `${data.Size} ${data.Type}`;
    append(li, span);
    append(li, span2);
    append(ul, li);
  })
})
.catch(function(error) {
  console.log(error);
});   