'use client';

import {useEffect, useState} from 'react';
import {useGame} from "@/app/contexts/GameContext";

const TIMER_DEFAULT = 'timer-default'
const TIMER_HEAL = 'timer-heal'
const TIMER_DAMAGE = 'timer-damage'

const AnimatedTimer = () => {
    const {state} = useGame()
    const [displayTime, setDisplayTime] = useState(state.time);
    const [isAnimating, setIsAnimating] = useState(false);
    const [timerState, setTimerState] = useState(TIMER_DEFAULT)

    useEffect(() => {
        if (state.time !== displayTime) {
            if (Math.abs(state.time - displayTime) <= 1 || Math.abs(state.time - displayTime) > 20) {
                setDisplayTime(state.time);
                setTimerState(TIMER_DEFAULT)
                return;
            }
            setIsAnimating(true);
            setTimerState((_) => {
                if (displayTime < state.time) return TIMER_HEAL; else return TIMER_DAMAGE
            })

            // Fast scroll animation
            const scrollSteps = 10; // Number of fast scroll steps
            const scrollDuration = 300; // Total animation duration in ms
            const stepDuration = scrollDuration / scrollSteps;

            let currentStep = 0;
            const scrollInterval = setInterval(() => {
                if (currentStep < scrollSteps) {
                    const step = Math.round(Math.abs(state.time - displayTime) / scrollSteps * currentStep)
                    const t = state.time > displayTime ? displayTime + step : displayTime - step
                    setDisplayTime(t);
                    currentStep++;
                } else {
                    clearInterval(scrollInterval);
                    setDisplayTime(state.time);
                    setTimerState(TIMER_DEFAULT)
                    setIsAnimating(false);
                }
            }, stepDuration);

            return () => clearInterval(scrollInterval);
        }
    }, [state.time, displayTime]);

    return (
        <span
            id="timer-count"
            className={`timer-count timer-display ${timerState} ${isAnimating ? 'animating' : ''}`}
        >
      {displayTime}
    </span>
    );
};

export default AnimatedTimer;