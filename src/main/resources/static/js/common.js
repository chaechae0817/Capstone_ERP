function toggleMenu(element) {
    // 클릭한 요소의 상위 메뉴를 확인
    const parentMenu = element.closest('li').parentNode;

    // 상위 메뉴(인사관리, 근태관리, 급여관리, 회사관리)를 선택
    const allMainMenus = document.querySelectorAll('.sidebar > ul > li > a');

    // 하위 메뉴가 있는지 확인
    const nestedMenu = element.nextElementSibling;

    // 상위 메뉴 클릭 시, 다른 상위 메뉴의 하위 메뉴를 닫음
    if (parentMenu === document.querySelector('.sidebar > ul')) {
        allMainMenus.forEach(menu => {
            const siblingMenu = menu.nextElementSibling;
            if (siblingMenu && siblingMenu !== nestedMenu) {
                siblingMenu.classList.remove('active');
            }
        });
    }

    // 클릭된 메뉴의 하위 메뉴를 토글
    if (nestedMenu) {
        nestedMenu.classList.toggle('active');
    }
}
