'use client';

import {useEffect, useState} from 'react';

interface AnimatedTimerProps {
    time: number;
}

const TIMER_DEFAULT = 'timer-default'
const TIMER_HEAL = 'timer-heal'
const TIMER_DAMAGE = 'timer-damage'

const AnimatedTimer = ({time}: AnimatedTimerProps) => {
    const [displayTime, setDisplayTime] = useState(time);
    const [isAnimating, setIsAnimating] = useState(false);
    const [state, setState] = useState(TIMER_DEFAULT)

    useEffect(() => {
        if (time !== displayTime) {
            if (Math.abs(time - displayTime) <= 1 || Math.abs(time - displayTime) > 20) {
                setDisplayTime(time);
                setState(TIMER_DEFAULT)
                return;
            }
            setIsAnimating(true);
            setState((_) => {
                if (displayTime < time) return TIMER_HEAL; else return TIMER_DAMAGE
            })

            // Fast scroll animation
            const scrollSteps = 10; // Number of fast scroll steps
            const scrollDuration = 300; // Total animation duration in ms
            const stepDuration = scrollDuration / scrollSteps;

            let currentStep = 0;
            const scrollInterval = setInterval(() => {
                if (currentStep < scrollSteps) {
                    const step = Math.round(Math.abs(time - displayTime) / scrollSteps * currentStep)
                    const t = time > displayTime ? displayTime + step : displayTime - step
                    setDisplayTime(t);
                    currentStep++;
                } else {
                    clearInterval(scrollInterval);
                    setDisplayTime(time);
                    setState(TIMER_DEFAULT)
                    setIsAnimating(false);
                }
            }, stepDuration);

            return () => clearInterval(scrollInterval);
        }
    }, [time, displayTime]);

    return (
        <span
            id="timer-count"
            className={`timer-count timer-display ${state} ${isAnimating ? 'animating' : ''}`}
        >
      {displayTime}
    </span>
    );
};

export default AnimatedTimer;