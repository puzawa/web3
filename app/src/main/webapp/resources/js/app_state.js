class AppState {
    async getPoints() {
        try {
            const response = await fetch('/web3/getPoints', {
                method: 'GET',
                headers: { 'Accept': 'application/json' }
            });

            if (!response.ok) {
                console.error(`Server responded with status ${response.status}`);
                return [];
            }
            let result;
            try {
                result = await response.json();
            } catch (jsonErr) {
                console.error("Response was not valid JSON:", jsonErr);
                return [];
            }
            if (!result) return [];
            return result;
        } catch (err) {
            console.error('Error calling getPoints:', err);
            return [];
        }
    }
    async getRs() {
        try {
            const response = await fetch('/web3/getRs', {
                method: 'GET',
                headers: { 'Accept': 'application/json' }
            });

            if (!response.ok) {
                console.error(`Server responded with status ${response.status}`);
                return [];
            }
            let result;
            try {
                result = await response.json();
            } catch (jsonErr) {
                console.error("Response was not valid JSON:", jsonErr);
                return [];
            }
            if (!result) return [];
            return result;
        } catch (err) {
            console.error('Error calling getPoints:', err);
            return [];
        }
    }

    async update() {
        const response = await this.getPoints();
        if (response != null) {
            this.points = response.points;
            this.maxR = response.maxR;
            this.enabledRs = response.enabledRs;
        } else {
            this.points = [];
            this.maxR = null;
            this.enabledRs = [];
        }
    }

    async updateRs(){
        const response = await this.getRs();
        if (response != null) {
            this.maxR = response.maxR;
            this.enabledRs = response.enabledRs;
        } else {
            this.maxR = null;
            this.enabledRs = [];
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