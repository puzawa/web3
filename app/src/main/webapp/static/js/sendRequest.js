const plane = document.getElementById('coordinate-plane')
const submitBtn = document.querySelector('.form-submit');

const YInput = document.querySelector('.form-input');

function handlePoint() {
    window.redrawCanvas();

    const y_str = YInput.value;
    const parts = y_str.split(".");
    const num = parseFloat(y_str);

    if (window.selectedXValue === null ||
        window.selectedRValue === null ||
        ['-', ''].includes(YInput.value) ||
        (!isNaN(num) && (num > 3 || num < -5)) ||
        parts.length > 2 ||
        ((num === 3 || num === -5) && (parts.length > 1 && !/^0*$/.test(parts[1])))
    )
    {
        submitBtn.disabled = true;
    }
    else {
        submitBtn.disabled = false;

        if (!(isFinite(selectedXValue) && isFinite(YInput.value) && isFinite(selectedRValue))) return;

        drawDot( selectedXValue, YInput.value, selectedRValue);
    }
}

YInput.addEventListener('paste', event => event.preventDefault());
YInput.addEventListener('input', function () {
    if (!/^-?\d*\.?\d*$/.test(this.value)) this.value = this.value.slice(0, -1);
    handlePoint();
});

async function sendData(x, y, r) {
    await fetch('/web2/controller',
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({x,y,r})
        })
        .then(function (response) {
            return response.text()
        }).then((html) => {
            document.body.innerHTML = html
        });
    console.log("data fetched")
}


async function exchange(event) {
    event.preventDefault();

    const X = selectedXValue;
    let Y = YInput.value;
    const R = selectedRValue;

    const startExec = Date.now();
    const sendTime = new Date().toLocaleTimeString();

    try {
        sendData(X.toString(), Y.toString(), R.toString());
        drawDot(plane, X, Y, R);

    } catch (err) {
        alert('Request failed: ' + err.message);
    }
}

plane.addEventListener("mousedown", async (event) => {
    event.preventDefault()

    let r = selectedRValue;
    if (r == null) {
        alert("Введите R");
        return
    }

    let canvasPos = canvas.getBoundingClientRect()
    const clickX = event.clientX - canvasPos.left
    const clickY = event.clientY - canvasPos.top

    console.log(clickX, clickY)
    const rVal = r;
    const x = ((clickX - canvas.cx) / (canvas.rw / rVal)).toFixed(4);
    const y = ((canvas.cy - clickY) / (canvas.rh / rVal)).toFixed(4);

    console.log(x, y, rVal)

    sendData(x.toString(), y.toString(), rVal.toString())
    drawDot(plane, x, y, rVal)
})
