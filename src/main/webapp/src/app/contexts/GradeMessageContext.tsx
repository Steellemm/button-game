// contexts/GradeMessageContext.tsx
'use client';

import React, { createContext, useContext, useState, ReactNode } from 'react';
import {GradeMessage} from "@/app/components/GradeMessage";

export type GradeType = 'EXCELLENT' | 'GREAT' | 'GOOD' | 'TERRIBLE' | 'SKIP';
// enum class RoundResult{
//     EXCELLENT, GREAT, GOOD, TERRIBLE, SKIP
// }
export interface GradeMessageInfo {
    id: string;
    type: GradeType;
    message: string;
    isVisible: boolean;
}

interface GradeMessageContextType {
    showMessage: (type: GradeType, message: string) => void;
}

const GradeMessageContext = createContext<GradeMessageContextType | undefined>(undefined);

export const useGradeMessage = () => {
    const context = useContext(GradeMessageContext);
    if (context === undefined) {
        throw new Error('useGradeMessage must be used within a GradeMessageProvider');
    }
    return context;
};

interface GradeMessageProviderProps {
    children: ReactNode;
}

export const GradeMessageProvider: React.FC<GradeMessageProviderProps> = ({ children }) => {
    const [messages, setMessages] = useState<GradeMessageInfo[]>([]);

    const showMessage = (type: GradeType, message: string) => {
        const id = Date.now().toString();
        const newMessage: GradeMessageInfo = {
            id,
            type,
            message,
            isVisible: true,
        };

        setMessages(prev => [...prev, newMessage]);

        setTimeout(() => {
            setMessages(prev => prev.filter(msg => msg.id !== id));
        }, 1500);
    };

    return (
        <GradeMessageContext.Provider value={{ showMessage }}>
            {children}
            <GradeMessage messages={messages} />
        </GradeMessageContext.Provider>
    );
};