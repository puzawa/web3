document.addEventListener("DOMContentLoaded", () => {

    let canvas = document.getElementById("coordinate-plane");
    window.canvasDrawer = new CanvasDrawer(canvas);
    canvasDrawer.redrawCanvas(1);
});