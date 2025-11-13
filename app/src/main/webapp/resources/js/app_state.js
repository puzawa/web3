class AppState {
    async getPoints() {
        try {
            const response = await fetch(`/web3/getPoints`, {method: 'GET', headers: {'Accept': 'application/json'}});
            if (!response.ok)
                throw new Error(`Server responded with ${response.status}`);

            const result = await response.json();
            return result || [];
        } catch (err) {
            console.error('Error calling getPoints:', err);
            return [];
        }
    }

    async update() {
        const response = await this.getPoints();
        if (response != null) {
            this.points = response.points;
            this.R = response.maxR;
        } else {
            this.points = [];
            this.R = null;
        }
    }

    async init() {
        await this.update();

    }

    setSubmitButtonDisabled(isDisabled) {
        const button = document.querySelector('.form-submit');
        if (button) {
            button.disabled = isDisabled;
        }
    }
}