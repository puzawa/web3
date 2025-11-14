class CanvasDrawer {
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');

        this.width = canvas.width = 600;
        this.height = canvas.height = 600;

        this.step = 60;
        this.cx = this.width / 2;
        this.cy = this.height / 2;
        this.rw = (this.width - this.step * 2) / 2;
        this.rh = (this.height - this.step * 2) / 2;

        this.line_length = 10;
        this.line_width = 4;

        this.canvas_color = 'rgba(181,162,241,0.67)';
        this.canvas_background_color = 'rgba(253,244,255,1.0)';
        this.grid_color = 'rgba(155,123,255,0.64)';

        this.hitCache = new Map();
    }

    drawTick(x1, y1, x2, y2, label, lx, ly, align = 'center') {
        const ctx = this.ctx;
        ctx.textAlign = align;
        ctx.fillText(label, lx, ly);
        ctx.beginPath();
        ctx.moveTo(x1, y1);
        ctx.lineTo(x2, y2);
        ctx.strokeStyle = 'black';
        ctx.lineWidth = this.line_width;
        ctx.stroke();
        ctx.closePath();
    }

    drawAxis() {
        const ctx = this.ctx;
        ctx.strokeStyle = 'black';
        ctx.lineWidth = 2;

        ctx.beginPath();
        ctx.moveTo(0, this.cy);
        ctx.lineTo(this.width, this.cy);
        ctx.stroke();

        ctx.beginPath();
        ctx.moveTo(this.cx, 0);
        ctx.lineTo(this.cx, this.height);
        ctx.stroke();
    }

    drawGrid() {
        const ctx = this.ctx;
        ctx.strokeStyle = this.grid_color;
        ctx.lineWidth = 1;

        ctx.beginPath();
        for (let x = 0; x <= this.width; x += this.step) {
            ctx.moveTo(x, 0);
            ctx.lineTo(x, this.height);
        }
        for (let y = 0; y <= this.height; y += this.step) {
            ctx.moveTo(0, y);
            ctx.lineTo(this.width, y);
        }
        ctx.stroke();
        ctx.closePath();
    }

    drawCoords(R) {
        const ctx = this.ctx;
        ctx.fillStyle = 'black';
        ctx.font = '32px Montserrat';
        ctx.textBaseline = 'bottom';

        R = ((R == null ) || (R === 0)) ? 'R' : parseFloat(R).toString();
        const R_half = R === 'R' ? 'R/2' : (parseFloat(R) / 2).toString();

        const shift = 10;

        const h = [
            [this.rw / 2, R_half],
            [this.rw, R],
            [-this.rw / 2, '-' + R_half],
            [-this.rw, '-' + R],
        ];
        h.forEach(([dx, label]) =>
            this.drawTick(
                this.cx + dx, this.cy - this.line_length,
                this.cx + dx, this.cy + this.line_length,
                label, this.cx + dx, this.cy - shift
            )
        );

        const v = [
            [-this.rh / 2, R_half],
            [-this.rh, R],
            [this.rh / 2, '-' + R_half],
            [this.rh, '-' + R],
        ];
        v.forEach(([dy, label]) =>
            this.drawTick(
                this.cx - this.line_length, this.cy + dy,
                this.cx + this.line_length, this.cy + dy,
                label, this.cx + shift, this.cy + dy, 'left'
            )
        );
    }

    drawCirclePart(x, y, r, arcStart, arcEnd, ccw, color) {
        const ctx = this.ctx;
        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.fillStyle = color;
        ctx.arc(x, y, r, arcStart, arcEnd, ccw);
        ctx.closePath();
        ctx.fill();
    }

    drawRect(x, y, w, h, color) {
        const ctx = this.ctx;
        ctx.beginPath();
        ctx.fillStyle = color;
        ctx.rect(x, y, w, h);
        ctx.fill();
        ctx.closePath();
    }

    drawTriangle(x, y, x2, y2, color) {
        const ctx = this.ctx;
        ctx.beginPath();
        ctx.fillStyle = color;
        ctx.moveTo(x, y);
        ctx.lineTo(x + x2, y);
        ctx.lineTo(x, y + y2);
        ctx.fill();
        ctx.closePath();
    }

    drawFigures(rw, rh, color) {
        this.drawCirclePart(this.cx, this.cy, rh / 2, Math.PI * 1.5, Math.PI * 2, false, color);
        this.drawRect(this.cx, this.cy, -rw, -rh / 2, color);
        this.drawTriangle(this.cx, this.cy, -rw / 2, rh, color);
    }

    drawDot(x, y, r, color = 'orange', outline = false, lineWidth = 2) {
        const ctx = this.ctx;
        ctx.save();
        ctx.translate(this.cx, this.cy);

        const plotX = x * (this.rw / r);
        const plotY = -y * (this.rh / r);

        ctx.beginPath();
        ctx.arc(plotX, plotY, 5, 0, 2 * Math.PI);

        if (outline) {
            ctx.strokeStyle = color;
            ctx.lineWidth = lineWidth;
            ctx.stroke();
        } else {
            ctx.fillStyle = color;
            ctx.fill();
        }

        ctx.restore();
        ctx.closePath();
    }


    drawDots(R, points = []) {
        if (!points || points.length === 0 || R === 0)
            return;

        const lastPoint = points[0];
        if (!lastPoint)
            return;

        for (const p of points) {
            const color = p.hit ? (p.r == R ? 'green' : 'limegreen') : (p.r == R ? 'red' : 'lightcoral');
            this.drawDot(p.x, p.y, R, color);
        }
        this.drawDot(lastPoint.x, lastPoint.y, R, 'black', true);
    }

    redrawCanvas() {
        const ctx = this.ctx;
        ctx.clearRect(0, 0, this.width, this.height);
        ctx.fillStyle = this.canvas_background_color;
        ctx.fillRect(0, 0, this.width, this.height);

        const points = appState.points;
        const maxR = appState.maxR;


        const erColorMap = {
            1: "rgba(180, 130, 255, 1.0)",
            2: "rgba(140, 0, 200, 1.0)",
            3: "rgba(200, 60, 255, 1.0)",
            4: "rgba(150, 80, 120, 1.0)",
            5: "rgba(255, 120, 255, 1.0)"
        };
        const color_main = erColorMap[maxR] ?? this.canvas_color;

        this.drawFigures(this.rw, this.rh, color_main);

        const enabledRs = appState.enabledRs;
        if (enabledRs != null) {
            for (const er of enabledRs) {
                const det = maxR / er;
                if (det === 1.0) continue;

                const color = erColorMap[er] ?? "rgba(200,200,200,0.5)";
                this.drawFigures(
                    this.rw / det,
                    this.rh / det,
                    color
                );
            }
        }


        this.drawGrid();
        this.drawCoords(maxR);
        this.drawAxis();
        this.drawDots(maxR, points);
    }
}
