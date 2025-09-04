'use client';

import { useState, useCallback } from 'react';

export type MessageType = 'good' | 'excellent' | 'warning' | 'error';

interface UseGradeMessageReturn {
    showMessage: (type: MessageType, message: string) => void;
    messageProps: {
        type: MessageType;
        message: string;
        isVisible: boolean;
        onHide: () => void;
    };
}

export const useGradeMessage = (): UseGradeMessageReturn => {
    const [isVisible, setIsVisible] = useState(false);
    const [message, setMessage] = useState('');
    const [type, setType] = useState<MessageType>('good');

    const showMessage = useCallback((newType: MessageType, newMessage: string) => {
        setType(newType);
        setMessage(newMessage);
        setIsVisible(true);
    }, []);

    const hideMessage = useCallback(() => {
        setIsVisible(false);
    }, []);

    return {
        showMessage,
        messageProps: {
            type,
            message,
            isVisible,
            onHide: hideMessage
        }
    };
};