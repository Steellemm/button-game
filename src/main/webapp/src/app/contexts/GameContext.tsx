'use client';

import React, {createContext, useContext, useReducer, ReactNode} from 'react';
import {useGameActions} from "@/app/hooks/useGameActions";

export interface Player {
    id: string;
    name: string;
    status?: 'normal' | 'passed' | 'explainer';
}

export interface Button {
    id: number;
    text: string;
    width: number;
    height: number;
    backgroundColor: number;
    backgroundTone: number;
    textColor: number;
    isRightClicked?: boolean;
}

export interface Boss {
    name: string;
    text: string;
    hp: number;
    maxHp: number;
}

export interface GameState {
    playerName: string;
    isConnected: boolean;
    players: Player[];
    round: number;
    time: number;
    level: number;
    hint: string;
    boss: Boss | null;
    isReady: boolean;
    buttons: Button[];
    backgroundColor: string,
    isTimerRunning: boolean
}

export type GameAction =
    | { type: 'SET_PLAYER_NAME'; payload: string }
    | { type: 'SET_CONNECTION_STATUS'; payload: boolean }
    | { type: 'UPDATE_PLAYERS'; payload: Player[] }
    | { type: 'SET_ROUND'; payload: number }
    | { type: 'SET_TIME'; payload: number }
    | { type: 'START_TIMER'; payload: number } // initial time
    | { type: 'STOP_TIMER' }
    | { type: 'DECREMENT_TIMER' }
    | { type: 'SET_LEVEL'; payload: number }
    | { type: 'SET_HINT'; payload: string }
    | { type: 'SET_BOSS'; payload: Boss | null }
    | { type: 'SET_READY_STATUS'; payload: boolean }
    | { type: 'SET_BUTTONS'; payload: Button[] }
    | { type: 'SET_PLAYERS_NORMAL' }
    | { type: 'SET_BACKGROUND_COLOR'; payload: string }
    | { type: 'SET_RIGHT_BUTTON'; payload: number }
    | { type: 'UPDATE_USER_STATUS'; payload: { playerId: string; status: 'normal' | 'passed' | 'explainer' } }
    | { type: 'DELETE_BUTTON'; payload: number }
    | { type: 'PLAYER_PASSED'; payload: string }
    | { type: 'RETURN_PLAYER_STATUS'; payload: string }
    | { type: 'COLOR_EXPLAINER'; payload: string }
    | { type: 'RESET_GAME_STATE' }
    | { type: 'PROCESS_GAME_MESSAGE'; payload: any };

const initialState: GameState = {
    playerName: '',
    isConnected: false,
    players: [],
    round: 0,
    time: 0,
    level: 1,
    hint: 'Wait other players',
    boss: null,
    isReady: false,
    buttons: [],
    backgroundColor: '#2c3e50',
    isTimerRunning: false,
};

function gameReducer(state: GameState, action: GameAction): GameState {
    switch (action.type) {
        case 'SET_PLAYER_NAME':
            return {...state, playerName: action.payload};

        case 'SET_CONNECTION_STATUS':
            return {...state, isConnected: action.payload};

        case 'UPDATE_PLAYERS':
            return {...state, players: action.payload};

        case 'SET_ROUND':
            return {...state, round: action.payload};

        case 'SET_TIME':
            return { ...state, time: action.payload };

        case 'START_TIMER':
            return {
                ...state,
                time: action.payload,
                isTimerRunning: true
            };

        case 'STOP_TIMER':
            return { ...state, isTimerRunning: false };

        case 'DECREMENT_TIMER':
            return { ...state, time: Math.max(0, state.time - 1) };

        case 'SET_LEVEL':
            return {...state, level: action.payload};

        case 'SET_HINT':
            return {...state, hint: action.payload};

        case 'SET_BOSS':
            return {...state, boss: action.payload};

        case 'SET_READY_STATUS':
            return {...state, isReady: action.payload};

        case 'SET_BUTTONS':
            return {...state, buttons: action.payload};

        case 'SET_PLAYERS_NORMAL':
            return {
                ...state, players: state.players.map(player => ({
                    ...player,
                    status: 'normal'
                }))
            };

        case 'SET_RIGHT_BUTTON':
            return {
                ...state, buttons: state.buttons.map(btn => btn.id === action.payload ? ({
                    ...btn,
                    isRightClicked: true
                }) : btn)
            }

        case 'DELETE_BUTTON':
            return {...state, buttons: state.buttons.filter(btn => btn.id !== action.payload)};

        case 'SET_BACKGROUND_COLOR':
            return {...state, backgroundColor: action.payload};

        case 'UPDATE_USER_STATUS':
            return {
                ...state,
                players: state.players.map(player =>
                    player.id === action.payload.playerId
                        ? {...player, status: action.payload.status}
                        : player
                ),
            };

        case 'PLAYER_PASSED':
            return {
                ...state,
                players: state.players.map(player =>
                    player.id === action.payload
                        ? {...player, status: 'passed' as const}
                        : player
                ),
            };

        case 'RETURN_PLAYER_STATUS':
            return {
                ...state,
                players: state.players.map(player =>
                    player.id === action.payload
                        ? {...player, status: 'normal' as const}
                        : player
                ),
            };

        case 'COLOR_EXPLAINER':
            return {
                ...state,
                players: state.players.map(player =>
                    player.id === action.payload
                        ? {...player, status: 'explainer' as const}
                        : player
                ),
            };

        case 'RESET_GAME_STATE':
            return {
                ...initialState,
                playerName: state.playerName, // Keep player name on reset
            };


        default:
            return state;
    }
}

const GameContext = createContext<{
    state: GameState;
    dispatch: React.Dispatch<GameAction>;
} | null>(null);

export const useGame = () => {
    const context = useContext(GameContext);
    if (!context) {
        throw new Error('useGame must be used within a GameProvider');
    }
    return context;
};

interface GameProviderProps {
    children: ReactNode;
}

export const GameProvider: React.FC<GameProviderProps> = ({children}) => {
    const [state, dispatch] = useReducer(gameReducer, initialState);

    return (
        <GameContext.Provider value={{state, dispatch}}>
            {children}
        </GameContext.Provider>
    );
};