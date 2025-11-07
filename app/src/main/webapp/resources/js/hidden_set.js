function setX(value) {
    document.getElementById('hiddenForm:x').value = value;
}

function setY(value) {
    document.getElementById('hiddenForm:y').value = value;
}

function setR(value) {
    document.getElementById('hiddenForm:r').value = value;
}

function setPoint(x, y, r) {
    setX(x);
    setY(y);
    setR(r);
}

function submitPoint() {
    document.getElementById('hiddenForm:saveButton').click();
}