'use client';

import { useState } from 'react';

interface AuthContainerProps {
    onSetName: (name: string) => void;
}

const AuthContainer = ({ onSetName }: AuthContainerProps) => {
    const [name, setName] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (name.trim()) {
            onSetName(name.trim());
        }
    };

    return (
        <div id="auth-container" className="auth-container">
            <form onSubmit={handleSubmit}>
                <label className="label" htmlFor="regName">
                    Your name:
                </label>
                <input
                    className="input"
                    type="text"
                    id="regName"
                    name="regName"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    autoFocus
                    maxLength={10}
                    required
                />
                <button className="name-button" type="submit">
                    OK
                </button>
            </form>
        </div>
    );
};

export default AuthContainer;