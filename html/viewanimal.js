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
        test = `${data.Name} ${data.Breed} ${data.Type}`;
        alert(test);
      })
    })
    .catch(function(error) {
      console.log(error);
    });