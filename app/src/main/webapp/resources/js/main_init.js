function previewPoint() {
    let last_point = {
        x: getSpinnerValue(),
        y: getTextInputValue(),
        result: "true"
    };
    window.canvasDrawer.redrawCanvas(getActiveCheckboxValue(), [], last_point);
}

function onClickCheckbox(clickedCheckbox) {
    let checkboxes = document.querySelectorAll('input.r-select-checkbox[type="checkbox"]');
    checkboxes.forEach(cb => {
        if (cb !== clickedCheckbox) {
            cb.checked = false;
        }
    });

    previewPoint();
}

function onClickSpinner(spinner) {
    previewPoint();
}

function onTextInput(input) {
    if (!input.dataset.lastValid) input.dataset.lastValid = '';
    const regex = /^-?\d*\.?\d*$/;
    if (regex.test(input.value)) {
        input.dataset.lastValid = input.value;
    } else {
        input.value = input.dataset.lastValid;
    }

    previewPoint();
}

function setTextInputNumericValidation(input) {
    input.addEventListener('input', () => onTextInput(input));

    input.addEventListener('focus', function () {
        this.dataset.lastValid = this.value;
    });

    input.addEventListener('paste', function (e) {
        e.preventDefault();
    });
}

document.addEventListener("DOMContentLoaded", () => {

    let canvas = document.getElementById("coordinate-plane");
    window.canvasDrawer = new CanvasDrawer(canvas);
    window.canvasDrawer.redrawCanvas(getActiveCheckboxValue());

    const input = document.getElementById('pointForm:textInput');
    if (input) setTextInputNumericValidation(input);
});

