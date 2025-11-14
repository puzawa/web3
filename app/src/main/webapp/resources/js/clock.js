class Clock {
    #hour;
    #minute;
    #second;

    constructor({hour, minute, second}) {
        this.#hour = hour;
        this.#minute = minute;
        this.#second = second;
    }

    animate(timeString) {
        const [h, m, s] = timeString.split(":").map(Number);

        const circleDeg = 360;
        const hoursInCircle = 12;
        const minutesInHour = 60;
        const minutesInCircle = 60;
        const secondsInCircle = 60;
        const oneHourDeg = circleDeg / hoursInCircle;
        const oneMinuteDeg = circleDeg / minutesInCircle;
        const oneSecondDeg = circleDeg / secondsInCircle;

        const hourDeg = ((h % hoursInCircle) + m / minutesInHour) * oneHourDeg;
        const minuteDeg = m * oneMinuteDeg;
        const secondDeg = s * oneSecondDeg;

        this.#hour.style.transform = `rotate(${hourDeg}deg)`;
        this.#minute.style.transform = `rotate(${minuteDeg}deg)`;
        this.#second.style.transform = `rotate(${secondDeg}deg)`;
    }
}