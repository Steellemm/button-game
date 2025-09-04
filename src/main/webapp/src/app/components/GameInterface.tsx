'use client';

import {Boss} from './GameContainer';
import HtmlEntityDecoder from "@/app/components/HtmlEntityDecoder";
import AnimatedTimer from "@/app/components/AnimatedTimer";
import {useGame} from "@/app/contexts/GameContext";

interface GameInterfaceProps {
    round: number;
    time: number;
    level: number;
    hint: string;
    boss: Boss | null;
    isReady: boolean;
    backgroundColor: string;
    onReadyClick: () => void;
    onButtonClick: (buttonId: number) => void;
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
                           level,
                           hint,
                           boss,
                           isReady,
                           backgroundColor,
                           onReadyClick,
                           onButtonClick
                       }: GameInterfaceProps) => {
    const {state} = useGame()
    return (
        <div className="container">
            <div id="header-div" className="header-div">
                <div className="statistic-div">
                    <div><AnimatedTimer/></div>
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
                {state.buttons.length === 0 ? (
                    <div
                        className={`button ${isReady ? 'ready-button' : 'not-ready-button'}`}
                        id="status-button"
                        onClick={onReadyClick}
                    >
                        {isReady ? 'Ready' : 'Not ready'}
                    </div>
                ) : (
                    state.buttons.map((btn) => {
                        const bgColor = colorMap.background[btn.backgroundColor as keyof typeof colorMap.background];
                        const tone = btn.backgroundTone;
                        const backgroundColor = tone === 100
                            ? bgColor
                            : `color-mix(in srgb, ${bgColor} ${tone}%, black)`;
                        const rightClicked = btn.isRightClicked ? '0 0 25px rgb(62, 225, 6)' : 'none'

                        const textColor = colorMap.text[btn.textColor as keyof typeof colorMap.text];

                        return (
                            <div
                                key={btn.id}
                                className={`button game-button ${btn.isRightClicked ? 'button-right-clicked' : ''}`}
                                data-id={btn.id}
                                style={{
                                    width: `${(80 * btn.width) / 100}px`,
                                    height: `${(80 * btn.height) / 100}px`,
                                    backgroundColor,
                                    boxShadow: rightClicked,
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