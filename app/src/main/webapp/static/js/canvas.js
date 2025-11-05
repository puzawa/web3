const canvas = document.getElementById('coordinate-plane');
const ctx = canvas.getContext('2d');

canvas.width = 600;
canvas.height = 600;
const step = 60; //better if w|h / n
const cx = canvas.width/2;
const cy = canvas.height/2;

const rw = (canvas.width - step*2)/2;
const rh = (canvas.height - step*2)/2;

const line_length = 10;
const line_width = 4;

const canvas_color = 'rgba(181,162,241,0.67)'
const grid_color = 'rgba(155,123,255,0.64)'

canvas.step = step;
canvas.cx = cx;
canvas.cy = cy;
canvas.rw = rw;
canvas.rh = rh;
canvas.line_length = line_length;
canvas.line_width = line_width;

function GetR() {
    return window.selectedRValue;
}
function drawAxis() {
    ctx.beginPath();
    ctx.moveTo(0, canvas.height / 2);
    ctx.lineTo(canvas.width, canvas.height / 2);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 2;
    ctx.stroke();


    ctx.beginPath();
    ctx.moveTo(canvas.width / 2, 0);
    ctx.lineTo(canvas.width / 2, canvas.height);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 2;
    ctx.stroke();
}

function drawGrid() {
    ctx.beginPath();
    for (let x = 0; x < canvas.width + step; x += step) {
        ctx.moveTo(x, 0);
        ctx.lineTo(x, canvas.height);
        ctx.strokeStyle = grid_color;
        ctx.lineWidth = 1;
        ctx.stroke();
    }

    for (let y = 0; y < canvas.height + step; y += step) {
        ctx.moveTo(0, y);
        ctx.lineTo(canvas.width, y);
        ctx.strokeStyle = grid_color;
        ctx.lineWidth = 1;
        ctx.stroke();
    }
}

function drawCoords() {
    ctx.fillStyle = 'black';
    ctx.font = '1.5em Montserrat';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'bottom';

    let R_str = GetR();
    R_str = R_str == null ? 'R' : parseFloat(R_str).toString();
    let R_half_str = GetR();
    R_half_str = R_half_str == null ? 'R/2' : (parseFloat(R_str) /2).toString();


    //horizontal
    ctx.textAlign = 'center';
    ctx.fillText(R_half_str, cx + rw/2, cy);
    ctx.beginPath();
    ctx.moveTo(cx + rw/2, cy-line_length);
    ctx.lineTo(cx + rw/2,cy+line_length);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();

    ctx.fillText(R_str, cx + rw , cy );
    ctx.beginPath();
    ctx.moveTo(cx + rw, cy-line_length);
    ctx.lineTo(cx + rw,cy+line_length);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();

    ctx.fillText('-' + R_half_str, cx - rw/2, cy);
    ctx.beginPath();
    ctx.moveTo(cx - rw/2, cy-line_length);
    ctx.lineTo(cx - rw/2,cy+line_length);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();

    ctx.fillText('-' +R_str, cx - rw , cy );
    ctx.beginPath();
    ctx.moveTo(cx - rw, cy-line_length);
    ctx.lineTo(cx - rw,cy+line_length);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();

    //vertical
    const text_x_shift = 10;
    ctx.textAlign = 'left';
    ctx.fillText(R_half_str, cx +text_x_shift, cy - rh/2);
    ctx.beginPath();
    ctx.moveTo(cx -line_length, cy- rh/2);
    ctx.lineTo(cx+ line_length,cy-rh/2);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();

    ctx.fillText(R_str, cx +text_x_shift, cy - rh);
    ctx.beginPath();
    ctx.moveTo(cx -line_length, cy- rh);
    ctx.lineTo(cx+ line_length,cy-rh);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();

    ctx.fillText('-' + R_half_str, cx +text_x_shift, cy + rh/2);
    ctx.beginPath();
    ctx.moveTo(cx -line_length, cy + rh/2);
    ctx.lineTo(cx+ line_length,cy + rh/2);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();

    ctx.fillText('-' + R_str, cx +text_x_shift, cy + rh);
    ctx.beginPath();
    ctx.moveTo(cx -line_length, cy + rh);
    ctx.lineTo(cx+ line_length,cy + rh);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = line_width;
    ctx.closePath();
    ctx.stroke();
}

function drawCircle(x, y, r) {
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.fillStyle = canvas_color;
    ctx.arc(x, y, r, Math.PI, Math.PI*0.5, true);
    ctx.closePath();
    ctx.fill();
}

function drawRect(x1,y1,w,h) {
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.fillStyle = canvas_color;
    ctx.rect(x1, y1, w, h);
    ctx.closePath();
    ctx.fill();
}

function drawTriangle(x,y,x2,y2) {
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.fillStyle = canvas_color;
    ctx.lineTo(x+x2, y);
    ctx.lineTo(x, y+y2);
    ctx.closePath();
    ctx.fill();
}

function drawDotOutline(x, y, r, color = 'orange', lineWidth = 2) {
    ctx.beginPath();
    ctx.translate(canvas.width / 2, canvas.height / 2);

    let plotX = x * (canvas.rw / r);
    let plotY = -y * (canvas.rh / r);

    ctx.arc(plotX, plotY, 5, 0, 2 * Math.PI);

    ctx.strokeStyle = color;
    ctx.lineWidth = lineWidth;
    ctx.stroke();

    ctx.resetTransform();
    ctx.closePath();
}

function drawDot(x, y, r, color = 'orange'){

    ctx.beginPath();
    ctx.translate(canvas.width/2, canvas.height/2);
    let plotX = x*(canvas.rw/r);
    let plotY = -y*(canvas.rh/r);

    ctx.arc(plotX, plotY, 5, 0, 2*Math.PI);

    ctx.fillStyle = color;
    ctx.fill();

    ctx.resetTransform();
    ctx.closePath();
}

function drawLastDot() {
    let r =GetR();
    if(r != null && lastPoint != null) {

        for (const p of points) {
            const color = (p.result === 'true') ? 'green' : 'red';
            drawDot(p.x, p.y, r, color);
        }

        drawDotOutline(lastPoint.x, lastPoint.y, r, 'black');
    }
}

function redrawCanvas() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = '#fdf4ff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    drawCircle(cx, cy, rh/2);
    drawRect(cx, cy, rw/2, -rh);
    drawTriangle(cx, cy, -rw/2, -rh);
    drawGrid();
    drawCoords();
    drawAxis();

    drawLastDot();
}

redrawCanvas();
window.redrawCanvas = redrawCanvas;
