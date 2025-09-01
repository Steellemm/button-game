'use client';

import { Player } from './GameContainer';

interface PlayersContainerProps {
    players: Player[];
}

const PlayersContainer = ({ players }: PlayersContainerProps) => {
    const getPlayerStyle = (player: Player) => {
        switch (player.status) {
            case 'passed':
                return {
                    backgroundColor: '#27ae60', // Green for passed players
                    color: 'white',
                    border: '2px solid #1e8449'
                };
            case 'explainer':
                return {
                    backgroundColor: '#3498db', // Blue for explainer
                    color: 'white',
                    border: '2px solid #2980b9'
                };
            default:
                return {
                    backgroundColor: '#34495e', // Default color
                    color: 'azure',
                    border: '1px solid #2c3e50'
                };
        }
    };

    return (
        <div id="players-container" className="players-container">
            <h3 className="players-title">Players</h3>
            <div className="player-list" id="player-list">
                {players.map((player) => (
                    <div
                        key={player.id}
                        className="player-item"
                        data-id={player.id}
                        data-status={player.status || 'normal'}
                        style={getPlayerStyle(player)}
                    >
                        <span className="player-name">{player.name}</span>
                        {player.status === 'passed' && (
                            <span className="player-status-icon">✓</span>
                        )}
                        {player.status === 'explainer' && (
                            <span className="player-status-icon">⭐</span>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PlayersContainer;