function createNode(element) {
    return document.createElement(element);
}

function append(parent, el) {
  return parent.appendChild(el);
}

const ul = document.getElementById('authors');
const url = '../api';
fetch(url)
.then((resp) => resp.json())
.then(function(data) {
  return data.map(function(data) {
    let li = createNode('li'),
        span = createNode('span');
    span.innerHTML = `${data.First} ${data.ID}`;
    append(li, span);
    append(ul, li);
  })
})
.catch(function(error) {
  console.log(error);
});   