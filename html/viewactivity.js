function createNode(element) {
    return document.createElement(element);
  }
  
  function append(parent, el) {
    return parent.appendChild(el);
  }
  
document.getElementById("lookup").onclick=function(){
    let profileid = document.getElementById("profileid").value;
    document.getElementById("profileid").value = "";

    let adopt_endpoint = `../api/get/useradopt?profile_id=${profileid}`;
    let donation_endpoint = `../api/get/userdonation?profile_id=${profileid}`;
    
    fetch(adopt_endpoint).then((resp) => resp.json())
    .then(function(data) {
        let thepage = document.getElementById('adoptions');
        while (thepage.hasChildNodes()) {  
            thepage.removeChild(thepage.firstChild);
        } 
      return data.map(function(data) {
        info = document.getElementById('adoptions');
        
        adoptinfo = createNode('p');
        adoptinfo.innerHTML = `Animal ID of ${data.Animal_ID} adopted on ${data.Adoption_Date}`;
        append(info, adoptinfo);
      })
    })
    .catch(function(error) {
      console.log(error);
    });

    fetch(donation_endpoint).then((resp) => resp.json())
    .then(function(data) {
        let thepage = document.getElementById('donations');
        while (thepage.hasChildNodes()) {  
            thepage.removeChild(thepage.firstChild);
        } 
      return data.map(function(data) {
        info = document.getElementById('donations');
        
        adoptinfo = createNode('p');
        adoptinfo.innerHTML = `Animal ID of ${data.Animal_ID} was given $${data.Donation_Amount} on ${data.Donation_Date}`;
        append(info, adoptinfo);
      })
    })
    .catch(function(error) {
      console.log(error);
    });

}