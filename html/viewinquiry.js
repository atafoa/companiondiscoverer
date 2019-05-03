var idincrement = 1;

function createNode(element) {
    return document.createElement(element);
  }
  
  function append(parent, el) {
    return parent.appendChild(el);
  }
  
  const url = `../api/get/inquiry`;
    fetch(url)
    .then((resp) => resp.json())
    .then(function(data) {
      return data.map(function(data) {
        info = document.getElementById('inquiries');
        
        question = createNode('h3');
        question.innerHTML = `${data.Inquiry_Question}`;
        append(info, question);
        
        date = createNode('p');
        date.innerHTML = `Date: ${data.Inquiry_Date}`;
        append(info, date);        
  
        answer = createNode('input');
        answer.id=idincrement;
        answer.type="text"; 
        answer.placeholder="Enter Answer";
        append(info, answer);

        answerbutton = createNode('button');
        answerbutton.name = idincrement;
        answerbutton.onclick=function(){
            this.disabled = true;
            let answerval = document.getElementById(this.name).value;
            let post = `../api/post/inquiryAns?animal_id=${data.Animal_ID}&profile_id=${data.Profile_ID}&inquiry_answer=${answerval}&inquiry_date=${data.Inquiry_Date}`;
            fetch(post);
        };
        answerbutton.innerHTML = `Answer`;
        append(info, answerbutton);
        idincrement += 1;
      })
    })
    .catch(function(error) {
      console.log(error);
    });