let sock
let stompClient

let isReady = false
var timer

const $authContainer = $('#auth-container');
const $bossDiv = $('#boss-div');
const $bossButtonDiv = $('#boss-button-div');
const $bossButtonName = $('#boss-button-name');
const $bossButtonText = $('#boss-button-body-div');
const $bossButtonHp = $('#boss-button-hp');
const $bossButtonMaxHp = $('#boss-button-max-hp');
const $hintContainer = $('#hint-div');
const $canvasContainer = $('#canvas-container');
const $playerList = $('#player-list');

const colorMap = {
    background: {
        0: 'azure',
        1: 'black',
        2: 'green',
        3: 'red',
        4: 'blue',
        5: 'yellow'
    },
    text: {
        0: 'azure',
        1: 'black',
        2: 'green',
        3: 'red',
        4: 'blue',
        5: 'yellow'
    }
};

showBoss(null)
main()

function main() {
    let cookies = parseCookie()
    if (cookies['button-game-name'] === undefined) {
        $authContainer.show()
    } else {
        $authContainer.hide()
        let playerName = $('#regName')[0].value
        subscribe(playerName)
    }
}

function setName() {
    $authContainer.hide()
    let playerName = $('#regName')[0].value
    document.cookie = `button-game-name=${playerName}`
    subscribe(playerName)
}

function parseCookie() {
    if (document.cookie === '') {
        return {}
    }
    return document.cookie
        .split(';')
        .map(v => v.split('='))
        .reduce((acc, v) => {
            acc[decodeURIComponent(v[0].trim())] = decodeURIComponent(v[1].trim());
            return acc;
        }, {});
}

function subscribe() {
    if (sock !== undefined && stompClient !== undefined && !stompClient.connected) {
        sock = new SockJS("ws://localhost:8111/button");
        stompClient = Stomp.over(sock);
    }
    if (sock === undefined) {
        sock = new SockJS("/button");
    }
    if (stompClient === undefined) {
        stompClient = Stomp.over(sock);
    }
    if (!stompClient.connected) {
        stompClient.connect({}, () => {
            stompClient.subscribe('/players/lobby/player', payload => {
                processTopicMessage(JSON.parse(payload.body))
            });
            stompClient.subscribe("/lobby", payload => {
                processTopicMessage(JSON.parse(payload.body))
            });
        }, function(message) {
            if (message.startsWith("Whoops! Lost connection to")) {
                alert("Lost connection")
            }
        });
    }
}

function processTopicMessage(message) {
    if (message['type'] === 'ErrorMessage') {
        alert(message['message']);
    }
    if (message['type'] === 'PlayersNumberChangeEvent') {
        //changePlayersNumber(message.playersNumber)
        refreshPlayerList(message.players)
    }
    if (message['type'] === 'FoulEvent') {
        setTimer(message.leftTime)
        deleteButton(message.buttonId)
    }
    if (message['type'] === 'RightClickEvent') {
        rightClick(message.buttonId)
    }
    if (message['type'] === 'PlayerNotReadyEvent') {
        returnPlayerStatus(message.player.id)
    }
    if (message['type'] === 'PlayerPassEvent') {
        playerPassed(message.player.id)
    }
    if (message['type'] === 'GameOverEvent') {
        clearInterval(timer);
        timer = undefined
        changeReady(false)
        alert("Game Over")
        setHint("Wait other players")
        setBackground(0)
        setRound(0)
        setLevel(0)
        setTimer(0)
        createReadyButton()
        returnPlayersStatus()
    }
    if (message['type'] === 'NewLevelGameEvent') {
        setTimer(message.leftTime)
        setRound(message.round)
        setLevel(message.level)
        processResponse(message.buttons)
        returnPlayersStatus()
        colorExplainer(message.explainerId)
        setHint(message.hint)
        setBackground(message.background)
        showBoss(message.boss)
    }
    if (message['type'] === 'NewBossStateEvent') {
        changeBossState(message)
        processResponse(message.buttons)
    }
    if (message['type'] === 'ReadyEvent') {
        changeReady(message.ready)
    }
    console.log(message)
}

function clickReady(e) {
    stompClient.send('/app/lobby/input/ready', {}, JSON.stringify({ready: !isReady}))
}

// Handle button click
function handleButtonClick(id) {
    stompClient.send('/app/lobby/input/pressButton', {}, JSON.stringify({buttonId: id}))
}

function refreshPlayerList(players) {
    $playerList.empty()

    players.forEach(player => $playerList.append($(`
     <div class="player-item" data-id="${player.id}">
            <span class="player-name">${player.name}</span>
        </div>
    `)))
}


function setTimer(leftTime) {
    let countElement = $('#timer-count');
    countElement.text(leftTime)

    if (timer === undefined) {
        timer = setInterval(() => {
            let count = parseInt(countElement.text());
            count--
            countElement.text(count);
            if (count <= 0) {
                clearInterval(timer);
                timer = undefined
                console.log("Timer reached zero!");
            }
        }, 1000)
    }
}

function setRound(round) {
    $('#round-count').text(round)
}

function setLevel(level) {
    $('#level-count').text(level)
}

function createReadyButton() {
    $canvasContainer.html($(`
        <div class="button not-ready-button" id="status-button" onclick="clickReady()">
            Not ready
        </div>
    `))
}

function changeReady(ready) {
    isReady = ready
    const $statusButton = $('#status-button');
    if (ready) {
        $statusButton.text("Ready")
        $statusButton.css('backgroundColor', 'green')
    } else {
        $statusButton.text("Not ready")
        $statusButton.css('backgroundColor', 'red')
    }
}

function deleteButton(id) {
    $(`.button[data-id="${id}"]`).hide()
}

function rightClick(id) {
    $(`.button[data-id="${id}"]`).css('box-shadow', '0 0 25px rgb(62, 225, 6)')
}

function returnPlayersStatus() {
    $('.player-item').css('color', 'azure')
}

function returnPlayerStatus(id) {
    $('.player-item[data-id="' + id + '"]').css('color', 'azure')
}

function playerPassed(id) {
    $('.player-item[data-id="' + id + '"]').css('color', 'green')
}

function setHint(hint) {
    if (hint !== undefined) {
        $hintContainer.text(hint)
    }
}

function setBackground(colorId) {
    // boss fight
    if (colorId === 1) {
        $canvasContainer.css('backgroundColor', '#750202')
        return
    }
    // bonus level
    if (colorId === 2) {
        $canvasContainer.css('backgroundColor', '#2e2b00')
        return;
    }
    // default
    $canvasContainer.css('backgroundColor', '#2c3e50')
}

function colorExplainer(id) {
    if (id !== undefined) {
        $('.player-item[data-id="' + id + '"]').css('color', 'blue')
    }
}

function changeBossState(state) {
    $bossButtonText.html(state.text)
    $bossButtonHp.text(state.hp)
}

function showBoss(boss) {
    if (boss == null) {
        $bossButtonDiv.hide()
        $bossDiv.css('backgroundColor', 'rgba(0,0,0,0)')
        return
    }
    $bossButtonText.html(boss.text)
    $bossButtonHp.text(boss.maxHp)
    $bossButtonMaxHp.text(boss.maxHp)
    $bossButtonName.text(boss.name)
    $bossButtonDiv.show()
    $bossDiv.css('backgroundColor', '#750202')
}

function processResponse(buttons) {
    if (buttons == null) {
        return
    }
    // Clear container
    $canvasContainer.empty();

    // Shuffle buttons array
    const shuffledButtons = [...buttons];
    for (let i = shuffledButtons.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffledButtons[i], shuffledButtons[j]] = [shuffledButtons[j], shuffledButtons[i]];
    }

    // Create buttons
    $.each(shuffledButtons, function(index, btn) {
        const $buttonElement = $('<div>').addClass('button').attr('data-id', btn.id);

        // Set size (percentage of initial 80px)
        const width = (80 * btn.width) / 100;
        const height = (80 * btn.height) / 100;
        $buttonElement.css({
            width: `${width}px`,
            height: `${height}px`
        });

        // Set background color and tone
        const bgColor = colorMap.background[btn.backgroundColor];
        const tone = btn.backgroundTone;
        $buttonElement.css('backgroundColor',
            tone === 100 ? bgColor : `color-mix(in srgb, ${bgColor} ${tone}%, black)`);

        // Set text color
        $buttonElement.css('color', colorMap.text[btn.textColor]);

        // Set content
        $buttonElement.html(btn.text);

        // Add click event
        $buttonElement.on('click', function() {
            handleButtonClick(btn.id);
        });

        $canvasContainer.append($buttonElement);
    });
}

