let clock = null;

function updateLocalDateTime() {
    const timestampInput = document.getElementById('clockForm:serverTimestamp');
    if (!timestampInput || !timestampInput.textContent) return;

    const serverTimestamp = parseInt(timestampInput.textContent, 10);
    const date = new Date(serverTimestamp);

    const dateOptions = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        timeZone: 'Europe/Moscow'
    };

    const timeOptions = {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
        timeZone: 'Europe/Moscow'
    };

    const userLocale = navigator.language;

    const formattedDate = date.toLocaleDateString(userLocale, dateOptions);
    const formattedTime = date.toLocaleTimeString(userLocale, timeOptions);

    const dateSpan = document.getElementById('js-date');
    const timeSpan = document.getElementById('js-time');

    if (dateSpan) dateSpan.textContent = formattedDate;
    if (timeSpan) timeSpan.textContent = formattedTime;
}

function syncClockWithBackend() {
    const timeText = document.getElementById("js-time").innerText;
    if (!timeText) return;

    clock.animate(timeText);
}

function updateTime() {
    updateLocalDateTime();
    syncClockWithBackend();
}

window.updateTime = updateTime;

document.addEventListener('DOMContentLoaded', () => {
    clock = new Clock({
        hour: document.querySelector('.hour'),
        minute: document.querySelector('.minute'),
        second: document.querySelector('.second')
    });
    updateTime();
});