function getActiveCheckboxValue() {
    let checkboxes = document.querySelectorAll('input.r-select-checkbox[type="checkbox"]');
    for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            return checkboxes[i].dataset.label;
        }
    }
    return null;
}

function getSpinnerValue() {
    let spinner = document.querySelector('#pointForm\\:horizontalAfter_input');
    if (spinner) {
        let value = spinner.value.replace(',', '.');
        return parseFloat(value);
    }
    return null;
}

function getTextInputValue() {
    let textInput = document.querySelector('#pointForm\\:textInput');
    if (textInput) {
        return textInput.value;
    }
    return null;
}
function getPointsFromTable() {
    const table = document.querySelector('.table');
    if (!table) return [];

    const points = [];
    const rows = table.querySelectorAll('tbody tr');

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length >= 4) {
            const x = parseFloat(cells[0].textContent.replace(',', '.'));
            const y = parseFloat(cells[1].textContent.replace(',', '.'));
            const r = parseFloat(cells[2].textContent.replace(',', '.'));
            const isHitText = cells[3].textContent.trim().toLowerCase();
            const result = (isHitText === 'true');

            let point = { x, y, r, result };
            points.push(point);
        }
    });

    return points;
}
