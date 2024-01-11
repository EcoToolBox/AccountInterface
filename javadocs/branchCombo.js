function onSubmit() {
    console.log("run");
    const nameCombo = document.getElementById('name');
    const typeCombo = document.getElementById('target');
    window.location.href = `${typeCombo.value}/refs/heads/${nameCombo.value}/javadoc/index.html`;
}

async function onTypeChange() {
    const nameCombo = document.getElementById('name');
    const typeCombo = document.getElementById('target');

    document.getElementById("name-label").innerHTML = `${typeCombo.value} name`

    nameCombo.textContent = "";
    const results = typeCombo.value == 'Branch' ? await getBranches() : await getReleases();

    for (const resultIndex in results) {
        const result = results[resultIndex];
        const request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState != request.DONE) {
                return;
            }
            if (request.status != 200) {
                return;
            }
            const option = document.createElement('option');
            option.value = result;
            option.innerText = result;
            nameCombo.appendChild(option);
        }
        request.onerror = function () {
            console.log(`Do not have javadocs for " + ${typeCombo.value} + "/" + ${result}`);
        }
        request.open('GET', `https://econtoolbox.github.io/AccountInterface/javadocs/${typeCombo.value}/refs/heads/${result}/javadoc/index.html`);
        request.send(null);
    }
}

async function getBranches() {
    return new Promise(function (resolve, reject) {
        const request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if ((request.readyState == XMLHttpRequest.DONE || (request.readyState == XMLHttpRequest.LOADING && !!request.response)) && request.status == 200) {
                const json = JSON.parse(request.response);
                const names = json.map(result => result.name);
                resolve(names);
                return;
            }
        }
        request.onerror = function () {
            console.error("Failed: " + request.statusText);
            reject([]);
        }
        request.open('GET', 'https://api.github.com/repos/EconToolBox/AccountInterface/branches', true);
        request.send(null);
    });
}

async function getReleases() {
    return new Promise(function (resolve, reject) {
        const request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == XMLHttpRequest.DONE && request.status == 200) {
                const json = JSON.parse(request.response);
                const names = json.map(result => result.name);
                resolve(names);
                return;
            }
        }
        request.onerror = function () {
            console.error("Failed: " + request.statusText);
            reject([]);
        }
        request.open('GET', 'https://api.github.com/repos/EconToolBox/AccountInterface/releases', true);
        request.send(null);
    });
}