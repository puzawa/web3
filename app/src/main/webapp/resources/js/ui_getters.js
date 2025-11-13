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

