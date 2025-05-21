// SHOW MENU
const showMenu = (toggleId, navbarId,bodyId) =>{
    const toggle = document.getElementById(toggleId),
        navbar = document.getElementById(navbarId),
        bodypadding = document.getElementById(bodyId)

    if(toggle && navbar){
        toggle.addEventListener('click', ()=>{
            // APARECER MENU
            navbar.classList.toggle('show')
            // ROTATE TOGGLE
            toggle.classList.toggle('rotate')
            // PADDING BODY
            bodypadding.classList.toggle('expander')
        })
    }
}
showMenu('nav-toggle','navbar','body')

// LINK ACTIVE COLOR
const linkColor = document.querySelectorAll('.nav__link');
function colorLink(){
    linkColor.forEach(l => l.classList.remove('active'));
    this.classList.add('active');
}

linkColor.forEach(l => l.addEventListener('click', colorLink));

<!-- JavaScript para el dropdown de idiomas -->
document.addEventListener('DOMContentLoaded', function() {
    const languageDropdown = document.getElementById('languageDropdown');
    const languageMenu = document.getElementById('languageMenu');

    if (languageDropdown && languageMenu) {
        languageDropdown.addEventListener('click', function(e) {
            e.preventDefault();
            languageMenu.classList.toggle('show-dropdown');
            this.querySelector('.dropdown-icon').classList.toggle('rotate');
        });
        // Cerrar el menú cuando se hace clic fuera
        document.addEventListener('click', function(e) {
            if (!languageDropdown.contains(e.target) && !languageMenu.contains(e.target)) {
                languageMenu.classList.remove('show-dropdown');
                languageDropdown.querySelector('.dropdown-icon').classList.remove('rotate');
            }
        });
    }
});

