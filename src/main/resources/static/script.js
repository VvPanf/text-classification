let btn = document.querySelector('#btn');
let sidebar = document.querySelector('.sidebar');
let header = document.querySelector('.header');

btn.onclick = function () {
    sidebar.classList.toggle('active');
}