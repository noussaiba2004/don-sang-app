document.addEventListener('DOMContentLoaded', function () {
    let lastScrollTop = 0;
    const header = document.getElementById('main-header');

    window.addEventListener('scroll', function () {
        const currentScroll = window.pageYOffset || document.documentElement.scrollTop;

        if (currentScroll > lastScrollTop && currentScroll > 100) {
            header.classList.add('hide-header');
        } else {
            header.classList.remove('hide-header');
        }

        lastScrollTop = currentScroll <= 0 ? 0 : currentScroll;
    });
});
