'use client';

import {useState, useEffect, useRef} from 'react';
import AuthContainer from './AuthContainer';
import PlayersContainer from './PlayersContainer';
import GameInterface from './GameInterface';
import {Stomp} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import Cookies from 'js-cookie';

export interface Player {
    id: string;
    name: string;
    status?: 'normal' | 'passed' | 'explainer';
}

export interface Button {
    id: string;
    text: string;
    width: number;
    height: number;
    backgroundColor: number;
    backgroundTone: number;
    textColor: number;
}

export interface Boss {
    name: string;
    text: string;
    hp?: number;
    maxHp: number;
}

export interface GameMessage {
    type: string;
    message?: string;
    players?: Player[];
    playersNumber?: number;
    buttonId?: string;
    leftTime?: number;
    round?: number;
    level?: number;
    buttons?: Button[];
    explainerId?: string;
    hint?: string;
    background?: number;
    boss?: Boss;
    ready?: boolean;
    player?: Player;
    hp?: number;
    hpChange?: number;
    text?: string;
}

const GameContainer = () => {
    const [playerName, setPlayerName] = useState<string>('');
    const [isConnected, setIsConnected] = useState<boolean>(false);
    const [players, setPlayers] = useState<Player[]>([]);
    const [round, setRound] = useState<number>(0);
    const [time, setTime] = useState<number>(0);
    const [level, setLevel] = useState<number>(1);
    const [hint, setHint] = useState<string>('Wait other players');
    const [boss, setBoss] = useState<Boss | null>(null);
    const [isReady, setIsReady] = useState<boolean>(false);
    const [buttons, setButtons] = useState<Button[]>([]);
    const [backgroundColor, setBackgroundColor] = useState<string>('#2c3e50');

    const stompClientRef = useRef<any>(null);
    const timerRef = useRef<NodeJS.Timeout | null>(null);

    useEffect(() => {
        const savedName = Cookies.get('button-game-name');
        if (savedName) {
            setPlayerName(savedName);
            subscribe(savedName);
        }

        return () => {
            if (timerRef.current) {
                clearInterval(timerRef.current);
            }
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.disconnect();
            }
        };
    }, []);

    const subscribe = (name: string) => {
        const sock = new SockJS(`http://${window.location.hostname}:8111/button`);

        const stompClient = Stomp.over(sock);

        stompClient.connect({}, () => {
            setIsConnected(true);
            stompClient.subscribe('/players/lobby/player', (payload) => {
                processTopicMessage(JSON.parse(payload.body));
            });
            stompClient.subscribe('/lobby', (payload) => {
                processTopicMessage(JSON.parse(payload.body));
            });
        }, (error: any) => {
            if (error.toString().includes("Lost connection")) {
                alert("Lost connection");
            }
        });

        stompClientRef.current = stompClient;
    };


    useEffect(() => {
        console.log('Boss updated:', boss);
    }, [boss]); // This runs whenever boss changes

    const processTopicMessage = (message: GameMessage) => {
        console.log('message', message)
        switch (message.type) {
            case 'ErrorMessage':
                alert(message.message);
                break;
            case 'PlayersNumberChangeEvent':
                if (message.players) {
                    setPlayers(message.players);
                }
                break;
            case 'FoulEvent':
                if (message.leftTime !== undefined) {
                    setTimer(message.leftTime);
                }
                if (message.buttonId) {
                    deleteButton(message.buttonId);
                }
                break;
            case 'RightClickEvent':
                if (message.buttonId) {
                    rightClick(message.buttonId);
                }
                break;
            case 'PlayerNotReadyEvent':
                if (message.player) {
                    returnPlayerStatus(message.player.id);
                }
                break;
            case 'PlayerPassEvent':
                if (message.player) {
                    playerPassed(message.player.id);
                }
                break;
            case 'GameOverEvent':
                setPlayersNormal()
                clearTimer();
                setIsReady(false);
                alert("Game Over");
                setHint("Wait other players");
                setBackgroundColor('#2c3e50');
                setRound(0);
                setLevel(0);
                setTime(0);
                setButtons([]);
                setBoss(null);
                break;
            case 'NewLevelGameEvent':
                setPlayersNormal()
                if (message.leftTime !== undefined) {
                    setTimer(message.leftTime);
                }
                if (message.round !== undefined) {
                    setRound(message.round);
                }
                if (message.level !== undefined) {
                    setLevel(message.level);
                }
                if (message.buttons) {
                    processResponse(message.buttons);
                }
                if (message.hint) {
                    setHint(message.hint);
                }
                if (message.background !== undefined) {
                    setBackground(message.background);
                }
                if (message.boss) {
                    setBoss({...message.boss, hp: message.boss.maxHp});
                } else {
                    setBoss(null);
                }
                break;
            case 'NewBossStateEvent':
                setPlayersNormal()
                if (message.hpChange !== undefined && message.hp !== undefined) {
                    console.log('changeBossparams if', message.hp, message.text);
                    changeBossparams(message.hp, message.text);
                }

                if (message.boss) {
                    changeBossState(message.boss);
                }
                if (message.buttons) {
                    processResponse(message.buttons);
                }
                break;
            case 'ReadyEvent':
                if (message.ready !== undefined) {
                    setIsReady(message.ready);
                }
                break;
            default:
                console.log(message);
        }
    };

    const setTimer = (leftTime: number) => {
        setTime(leftTime);

        if (timerRef.current) {
            clearInterval(timerRef.current);
        }

        timerRef.current = setInterval(() => {
            setTime((prevTime) => {
                if (prevTime <= 1) {
                    clearTimer();
                    return 0;
                }
                return prevTime - 1;
            });
        }, 1000);
    };

    const clearTimer = () => {
        if (timerRef.current) {
            clearInterval(timerRef.current);
            timerRef.current = null;
        }
    };

    const deleteButton = (buttonId: string) => {
        setButtons(prev => prev.filter(btn => btn.id !== buttonId));
    };

    const rightClick = (buttonId: string) => {
        // This would need additional state to track highlighted buttons
        console.log('Right click on button:', buttonId);
    };

    const setPlayersNormal = () => {
        setPlayers(prev => prev.map(player => ({
            ...player,
            status: 'normal'
        })));
    }

    const returnPlayerStatus = (playerId: string) => {
        setPlayers(prev => prev.map(player =>
            player.id === playerId
                ? {...player, status: 'normal'}
                : player
        ));
    };

    const playerPassed = (playerId: string) => {
        setPlayers(prev => prev.map(player =>
            player.id === playerId
                ? {...player, status: 'passed'}
                : player
        ));
    };

    const setBackground = (colorId: number) => {
        switch (colorId) {
            case 1: // boss fight
                setBackgroundColor('#750202');
                break;
            case 2: // bonus level
                setBackgroundColor('#2e2b00');
                break;
            default:
                setBackgroundColor('#2c3e50');
        }
    };

    const changeBossHp = (hp: number) => {
        setBoss(boss => {
            if (boss) return {...boss, hp: hp};
            else return null;
        })
    }
    const changeBossText = (hp: number) => {
        setBoss(boss => {
            if (boss) return {...boss, hp: hp};
            else return null;
        })
    }

    const changeBossparams = (hp: number, text?: string) => {
        setBoss(boss => {
            if (boss) return {
                ...boss,
                hp: hp,
                text: text ? text : boss.text
            };
            else return null;
        })
    }
    const changeBossState = (bossState: Boss) => {
        setBoss(bossState);
    };

    const processResponse = (newButtons: Button[]) => {
        const shuffledButtons = [...newButtons].sort(() => Math.random() - 0.5);
        setButtons(shuffledButtons);
    };

    const handleSetName = (name: string) => {
        setPlayerName(name);
        Cookies.set('button-game-name', name);
        subscribe(name);
    };

    const handleReadyClick = () => {
        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.send(
                '/app/lobby/input/ready',
                {},
                JSON.stringify({ready: !isReady})
            );
        }
    };

    const handleButtonClick = (buttonId: string) => {
        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.send(
                '/app/lobby/input/pressButton',
                {},
                JSON.stringify({buttonId})
            );
        }
    };

    if (!playerName) {
        return <AuthContainer onSetName={handleSetName}/>;
    }

    return (
        <div className="game-container">
            <PlayersContainer players={players}/>
            <GameInterface
                round={round}
                time={time}
                level={level}
                hint={hint}
                boss={boss}
                isReady={isReady}
                buttons={buttons}
                backgroundColor={backgroundColor}
                onReadyClick={handleReadyClick}
                onButtonClick={handleButtonClick}
            />
        </div>
    );
};

export default GameContainer;