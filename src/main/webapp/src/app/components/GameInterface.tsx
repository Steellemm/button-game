'use client';

import {Button, Boss} from './GameContainer';
import HtmlEntityDecoder from "@/app/components/HtmlEntityDecoder";

interface GameInterfaceProps {
    round: number;
    time: number;
    level: number;
    hint: string;
    boss: Boss | null;
    isReady: boolean;
    buttons: Button[];
    backgroundColor: string;
    onReadyClick: () => void;
    onButtonClick: (buttonId: string) => void;
}

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

const GameInterface = ({
                           round,
                           time,
                           level,
                           hint,
                           boss,
                           isReady,
                           buttons,
                           backgroundColor,
                           onReadyClick,
                           onButtonClick
                       }: GameInterfaceProps) => {
    return (
        <div className="container">
            <div id="header-div" className="header-div">
                <div className="statistic-div">
                    <div className="timer-count"><span id="timer-count">{time}</span></div>
                    <div className="game-info-div">
                        <div>Level: <span id="level-count">{level}</span></div>
                        <div>Round: <span id="round-count">{round}</span></div>
                    </div>
                </div>
                <div id="hint-div" className="hint-div">{hint}</div>
                <div className="boss-div">
                    {boss && (
                        <div id="boss-button-div" className="boss-button-div">
                            <div id="boss-button-name-div">
                                <span id="boss-button-name">{boss.name}</span>
                            </div>
                            <div id="boss-button-body-div">
                                <HtmlEntityDecoder html={boss.text}/>
                            </div>
                            <div id="boss-button-hp-div">
                                <span id="boss-button-hp">{boss.hp}</span>/
                                <span id="boss-button-max-hp">{boss.maxHp}</span>
                            </div>
                        </div>
                    )}
                </div>
            </div>

            <div
                className="canvas-container"
                id="canvas-container"
                style={{backgroundColor}}
            >
                {buttons.length === 0 ? (
                    <div
                        className={`button ${isReady ? 'ready-button' : 'not-ready-button'}`}
                        id="status-button"
                        onClick={onReadyClick}
                    >
                        {isReady ? 'Ready' : 'Not ready'}
                    </div>
                ) : (
                    buttons.map((btn) => {
                        const bgColor = colorMap.background[btn.backgroundColor as keyof typeof colorMap.background];
                        const tone = btn.backgroundTone;
                        const backgroundColor = tone === 100
                            ? bgColor
                            : `color-mix(in srgb, ${bgColor} ${tone}%, black)`;

                        const textColor = colorMap.text[btn.textColor as keyof typeof colorMap.text];

                        return (
                            <div
                                key={btn.id}
                                className="button game-button"
                                data-id={btn.id}
                                style={{
                                    width: `${(80 * btn.width) / 100}px`,
                                    height: `${(80 * btn.height) / 100}px`,
                                    backgroundColor,
                                    color: textColor
                                }}
                                onClick={() => onButtonClick(btn.id)}
                            >
                                <HtmlEntityDecoder html={btn.text}/>
                            </div>
                        );
                    })
                )}
            </div>
        </div>
    );
};

export default GameInterface;