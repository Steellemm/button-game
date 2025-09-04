'use client';

import {Button, Player, useGame} from '@/app/contexts/GameContext';

export const useGameActions = () => {
    const { dispatch } = useGame();

    const setPlayerName = (name: string) => {
        dispatch({ type: 'SET_PLAYER_NAME', payload: name });
    };

    const setConnectionStatus = (isConnected: boolean) => {
        dispatch({ type: 'SET_CONNECTION_STATUS', payload: isConnected });
    };

    const playerPassed = (playerId: string) => {
        dispatch({ type: 'PLAYER_PASSED', payload: playerId });
    };

    const returnPlayerStatus = (playerId: string) => {
        dispatch({ type: 'RETURN_PLAYER_STATUS', payload: playerId });
    };

    const colorExplainer = (explainerId: string) => {
        dispatch({ type: 'COLOR_EXPLAINER', payload: explainerId });
    };

    const setReadyStatus = (isReady: boolean) => {
        dispatch({ type: 'SET_READY_STATUS', payload: isReady });
    };

    const setButtons = (buttons: Button[]) => {
        dispatch({ type: 'SET_BUTTONS', payload: buttons });
    };

    const resetGameState = () => {
        dispatch({ type: 'RESET_GAME_STATE' });
    };

    const setTime = (time: number) => {
        dispatch({ type: 'SET_TIME', payload: time });
    };

    const startTimer = (time: number) => {
        dispatch({type: 'START_TIMER', payload: time})
    }

    const stopTimer = () => {
        dispatch({type: 'STOP_TIMER'})
    }

    const decrementTimer = () => {
        dispatch({type: 'DECREMENT_TIMER'})
    }

    const deleteButton = (buttonId: number) => {
        dispatch({ type: 'DELETE_BUTTON', payload: buttonId });
    };

    const rightButtonClick = (buttonId: number) => {
        dispatch({ type: 'SET_RIGHT_BUTTON', payload: buttonId });
    };

    const updatePlayers = (players: Player[]) => {
        dispatch({ type: 'UPDATE_PLAYERS', payload: players });
    }

    const updateUserStatus = (playerId: string, status: 'normal' | 'passed' | 'explainer') => {
        dispatch({
            type: 'UPDATE_USER_STATUS',
            payload: { playerId, status }
        });
    };

    const setPlayersNormal = () => {
        dispatch({
            type: 'SET_PLAYERS_NORMAL'
        })
    }
    // Add more actions as needed...

    return {
        setPlayerName,
        setConnectionStatus,
        playerPassed,
        returnPlayerStatus,
        colorExplainer,
        setReadyStatus,
        resetGameState,
        setTime,
        deleteButton,
        updatePlayers,
        updateUserStatus,
        setPlayersNormal,
        setButtons,
        rightButtonClick,startTimer, stopTimer, decrementTimer
    };
};