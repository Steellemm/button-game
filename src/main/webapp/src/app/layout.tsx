import type {Metadata} from 'next';
import {Inter} from 'next/font/google';
import '../styles/globals.css';
import {GradeMessageProvider} from "@/app/contexts/GradeMessageContext";
import {GameProvider} from "@/app/contexts/GameContext";

const inter = Inter({subsets: ['latin']});

export const metadata: Metadata = {
    title: 'Button Game',
    description: 'A multiplayer button clicking game',
};

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en">
        <body className={inter.className}>
        <GameProvider>
            <GradeMessageProvider>{children}</GradeMessageProvider>
        </GameProvider>
        </body>
        </html>
    );
}