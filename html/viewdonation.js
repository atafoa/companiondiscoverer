function createNode(element) {
    return document.createElement(element);
  }
  
  function append(parent, el) {
    return parent.appendChild(el);
  }
  
  const url = `../api/get/donation`;
    fetch(url)
    .then((resp) => resp.json())
    .then(function(data) {
      return data.map(function(data) {
        info = document.getElementById('donations');
        
        record = createNode('p');
        record.innerHTML = `User ${data.Profile_ID} donated $${data.Donation_Amount} to Animal ID ${data.Animal_ID}.`;
        append(info, record);

        date = createNode('p');
        date.innerHTML = `Date: ${data.Donation_Date}`;
        append(info, date);        
  
        linebreak = createNode('br');
        append(info, linebreak);
      })
    })
    .catch(function(error) {
      console.log(error);
    });