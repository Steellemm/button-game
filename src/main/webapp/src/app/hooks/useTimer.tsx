'use client';

import { useEffect, useRef, useCallback } from 'react';
import { useGame } from '@/app/contexts/GameContext';
import {useGameActions} from "@/app/hooks/useGameActions";

export const useTimer = () => {
    const { state } = useGame();
    const { setTime, startTimer, stopTimer, decrementTimer } = useGameActions();
    const timerRef = useRef<NodeJS.Timeout | null>(null);

    // Clear timer on unmount
    useEffect(() => {
        return () => {
            if (timerRef.current) {
                clearInterval(timerRef.current);
                timerRef.current = null;
            }
        };
    }, []);

    const clearTimer = useCallback(() => {
        if (timerRef.current) {
            clearInterval(timerRef.current);
            timerRef.current = null;
            stopTimer();
        }
    }, [stopTimer]);

    const setTimer = useCallback((leftTime: number) => {
        // Clear any existing timer
        clearTimer();

        // Set the initial time
        setTime(leftTime);

        // Start new timer if time is positive
        if (leftTime > 0) {
            startTimer(leftTime);

            timerRef.current = setInterval(() => {
                decrementTimer();
            }, 1000);
        }
    }, [clearTimer, setTime, startTimer, decrementTimer]);

    // Auto-stop timer when it reaches 0
    useEffect(() => {
        if (state.time <= 0 && state.isTimerRunning) {
            clearTimer();
        }
    }, [state.time, state.isTimerRunning, clearTimer]);

    return {
        time: state.time,
        isTimerRunning: state.isTimerRunning,
        setTimer,
        clearTimer,
    };
};