window.addEventListener('load', function(){

    let startCalendar = document.querySelector('.start');
    let startSubCalendar = document.querySelector('.startChild');
    let startModal = document.querySelector('.startModal');

    let endCalendar = document.querySelector('.end');
    let endSubCalendar = document.querySelector('.endChild');
    let endModal = document.querySelector('.endModal');

    startModal.addEventListener('click', function(){
        endCalendar.classList.remove('calActive');
        endSubCalendar.classList.remove('active');
        endModal.classList.remove('hidden');

        startCalendar.classList.toggle('calActive');
        startSubCalendar.classList.toggle('active');
        startModal.classList.toggle('hidden');
    })

    endModal.addEventListener('click', function(){
        startCalendar.classList.remove('calActive');
        startSubCalendar.classList.remove('active');
        startModal.classList.remove('hidden');

        endCalendar.classList.toggle('calActive');
        endSubCalendar.classList.toggle('active');
        endModal.classList.toggle('hidden');
    })

})