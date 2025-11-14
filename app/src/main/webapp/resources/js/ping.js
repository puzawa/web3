let serverWasDown = false;

function checkServer() {
    fetch('/web3/ping', {method: 'GET'})
        .then(res => {
            if (!res.ok) throw new Error();

            if (serverWasDown) {
                PF("serverDownDialog").hide();
                PF("serverBackDialog").show();
            }
        })
        .catch(() => {
            serverWasDown = true;
            PF("serverDownDialog").show();
            PF("serverBackDialog").hide();
        });
}
