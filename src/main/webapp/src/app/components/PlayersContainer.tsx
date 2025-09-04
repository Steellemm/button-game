'use client';

import { useState } from 'react';
import {useGame} from "@/app/contexts/GameContext";

const PlayersContainer = () => {
    const { state } = useGame()
    const [isVisible, setIsVisible] = useState(false);

    return (
        <>
            {/* Mobile toggle button */}
            <button
                className={"mobileToggle"}
                onClick={() => setIsVisible(!isVisible)}
                aria-label="Toggle players list"
            >
                👥
            </button>

            <div
                id="players-container"
                className={`${"playersContainer"} ${isVisible ? "visible" : ''}`}
            >
                <h3 className={"playersTitle"}>Players</h3>
                <div className={"playerList"} id="player-list">
                    {state.players.map((player) => (
                        <div
                            key={player.id}
                            className={"playerItem"}
                            data-status={player.status || 'normal'}
                        >
                            <span className={"playerName"}>{player.name}</span>
                            {player.status === 'passed' && (
                                <span className={"playerStatusIcon"}>✓</span>
                            )}
                            {player.status === 'explainer' && (
                                <span className={"playerStatusIcon"}>⭐</span>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
};

export default PlayersContainer;