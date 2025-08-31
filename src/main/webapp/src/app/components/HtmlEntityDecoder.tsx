'use client';

import { useMemo } from 'react';

interface HtmlEntityDecoderProps {
    html: string;
    className?: string;
}

const HtmlEntityDecoder = ({ html, className }: HtmlEntityDecoderProps) => {
    const decodedHtml = useMemo(() => {
        // Create a temporary element to decode HTML entities
        const tempElement = document.createElement('div');
        tempElement.innerHTML = html;
        return tempElement.textContent || tempElement.innerText || '';
    }, [html]);

    return <span className={className}>{decodedHtml}</span>;
};

export default HtmlEntityDecoder;