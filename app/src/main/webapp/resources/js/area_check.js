async function getPoints(R) {
    try {
        const response = await fetch(`/web3/getPoints?r=${encodeURIComponent(R)}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Server responded with ${response.status}`);
        }

        const result = await response.json();

        return result.points || [];
    } catch (err) {
        console.error('Error calling getPoints:', err);
        return [];
    }
}
