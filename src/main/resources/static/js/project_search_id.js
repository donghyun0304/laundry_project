document.querySelector('#memberPhonenumber').addEventListener('input',function(){
    document.querySelector('.validation-phonenumber').value='';
})

function phoneNum(){
    let num = document.querySelector('#memberPhonenumber');
    if (num.value.length == 3 || num.value.length == 8){
        num.value += "-";
        document.querySelector('#sendSmsBtn').disabled = false;
    }
}

// 인증번호 보내기
function sendSms(){
    let subPhonenumber = document.querySelector('#memberPhonenumber');
    let phonenumber = subPhonenumber.value.replace(/-/gi,'');
    let sendSmsBtn = document.querySelector('#sendSmsBtn');
    let resultMsg = document.querySelector('#phonenumber-check-warn');


    fetch("/sendSms?phonenumber="+phonenumber)
    .then(response => response.json())
    .then(map => {
    		alert("인증번호가 전송되었습니다.");
    		phone_check_number = map.number;
    		//document.querySelector('.phonenumber-check-input').disabled = false;
    		document.querySelector('.phonenumber-check-button').addEventListener('click', function(){
    			let inputCode = document.querySelector('.phonenumber-check-input');

    			if(phone_check_number == inputCode.value){
    		        alert('인증되었습니다.');
    		        document.querySelector('.validation-phonenumber').value=1;
    			}else{
    				resultMsg.innerHTML = '인증번호가 불일치 합니다. 다시 확인해주세요!';
    		        resultMsg.style.color = 'red';
    			}
    		})
    	})
}
