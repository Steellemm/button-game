// components/GradeMessage.tsx
'use client';

import React from 'react';
import {GradeMessageInfo as GradeMessageType} from '@/app/contexts/GradeMessageContext';

interface GradeMessageProps {
    messages: GradeMessageType[];
}

export const GradeMessage: React.FC<GradeMessageProps> = ({ messages }) => {
    if (messages.length === 0) return null;

    return (
        <div className="grade-message-container">
            {messages.map((message) => (
                <div
                    key={message.id}
                    className={`grade-message grade-message-${message.type}`}
                >
                    <div className="grade-message-content">
                        <p className="grade-message-text">{message.message}</p>
                    </div>
                </div>
            ))}
        </div>
    );
};