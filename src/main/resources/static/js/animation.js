
function blink(buttonId) {
    function smoothBlink() {
        $(`.button[data-id="${buttonId}"]`)
            .animate({opacity: 0.1}, 300)
            .animate({opacity: 1}, 500, function () {
                setTimeout(smoothBlink, 500);
            });
    }

    smoothBlink();
}