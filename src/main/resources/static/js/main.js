let sock
let stompClient

let isReady = false
var timer

const $canvasContainer = $('#canvas-container');
const colorMap = {
    background: {
        0: 'azure',
        1: 'black',
        2: 'green',
        3: 'red',
        4: 'blue'
    },
    text: {
        0: 'azure',
        1: 'black',
        2: 'green',
        3: 'red',
        4: 'blue'
    }
};

subscribe()

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
        changePlayersNumber(message.playersNumber)
    }
    if (message['type'] === 'FoulEvent') {
        setTimer(message.leftTime)
        deleteButton(message.buttonId)
    }
    if (message['type'] === 'GameOverEvent') {
        clearInterval(timer);
        timer = undefined
        changeReady(false)
        alert("Game Over")
    }
    if (message['type'] === 'StartChooserGameEvent') {
        setTimer(message.leftTime)
        setRound(message.round)
        processResponse(message.buttons)
    }
    if (message['type'] === 'StartExplainGameEvent') {
        setTimer(message.leftTime)
        setRound(message.round)
        processResponse([message.button])
    }
    if (message['type'] === 'ReadyEvent') {
        changePlayersNumber(message.playersNumber)
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

function changePlayersNumber(playersNumber) {
    $('#players-count').text(playersNumber)
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

function changeReady(ready) {
    isReady = ready
    if (ready) {
        $('#ready-flag').text("Ready")
    } else {
        $('#ready-flag').text("Not ready")
    }
}

function deleteButton(id) {
    $(`[data-id="${id}"]`).hide()
}

function processResponse(buttons) {
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

