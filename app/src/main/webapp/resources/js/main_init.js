function setSubmitButtonDisabled(isDisabled) {
    const button = document.querySelector('.form-submit');
    if (button) {
        button.disabled = isDisabled;
    }
}

async function drawPoints() {
    await window.canvasDrawer.redrawCanvas(getActiveCheckboxValue());
}

async function handlePoint() {

    const y_str = getTextInputValue();
    const parts = y_str.split(".");
    const num = parseFloat(y_str);
    let selectedXValue = getSpinnerValue();
    let selectedRValue = getActiveCheckboxValue();
    let selectedRNum = parseInt(selectedRValue);

    if (selectedXValue === null ||
        selectedRValue === null ||
        ['-', ''].includes(y_str) ||
        (!isNaN(num) && (num > 5 || num < -3)) ||
        parts.length > 2 ||
        ((num === 5 || num === -3) && (parts.length > 1 && !/^0*$/.test(parts[1])))
    ) {
        setSubmitButtonDisabled(true);
    } else {
        await drawPoints();
        setSubmitButtonDisabled(false);

        if (!(isFinite(selectedXValue) && isFinite(y_str) && isFinite(selectedRNum))) return;

        setPoint(selectedXValue, y_str, selectedRValue);
        window.canvasDrawer.drawDot(selectedXValue, y_str, selectedRValue);
    }
}

async function onClickCheckbox(clickedCheckbox) {
    let checkboxes = document.querySelectorAll('input.r-select-checkbox[type="checkbox"]');
    checkboxes.forEach(cb => {
        if (cb !== clickedCheckbox) {
            cb.checked = false;
        }
    });

    await handlePoint();
}

async function onClickSpinner(spinner) {
    await handlePoint();
}

async function onTextInput(input) {
    if (!input.dataset.lastValid) input.dataset.lastValid = '';
    const regex = /^-?\d*\.?\d*$/;
    if (regex.test(input.value)) {
        input.dataset.lastValid = input.value;
    } else {
        input.value = input.dataset.lastValid;
    }

    await handlePoint();
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

function setCanvasOnClick() {
    window.canvasDrawer.canvas.addEventListener("mousedown", async (event) => {
        event.preventDefault()

        let r = getActiveCheckboxValue();
        if (r == null) {
            alert("Введите R");
            return
        }
        const plane = window.canvasDrawer;

        let canvasPos = plane.canvas.getBoundingClientRect()
        const clickX = event.clientX - canvasPos.left
        const clickY = event.clientY - canvasPos.top

        const x = ((clickX - plane.cx) / (plane.rw / r)).toFixed(4);
        const y = ((plane.cy - clickY) / (plane.rh / r)).toFixed(4);

        setPoint(x, y, r);
        submitPoint();
        await handlePoint();
        //window.canvasDrawer.drawDot(x, y, r)
    })
}

document.addEventListener("DOMContentLoaded", async () => {


    let canvas = document.getElementById("coordinate-plane");
    window.canvasDrawer = new CanvasDrawer(canvas);
    await drawPoints();
    setCanvasOnClick();

    const input = document.getElementById('pointForm:textInput');
    if (input) setTextInputNumericValidation(input);

   await handlePoint();
});

async function handlePointAjax(data) {
    if (data.status === "success") {
        await handlePoint();
    }
}