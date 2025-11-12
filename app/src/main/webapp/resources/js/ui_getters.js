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

