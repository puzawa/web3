async function checkHit(mouseX, mouseY, R) {
    try {
        const response = await fetch('/web3/checkArea', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                x: mouseX,
                y: mouseY,
                r: R
            })
        });

        if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const result = await response.json();
        return result.hit;
    } catch (err) {
        console.error('Error calling hitCheck:', err);
        return false;
    }
}