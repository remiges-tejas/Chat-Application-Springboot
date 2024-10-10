import  { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [messageContent, setMessageContent] = useState('');
    const [username, setUsername] = useState('');
    const [recipient, setRecipient] = useState(''); // For private messages
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        connect(); // Connect to WebSocket when component mounts.
    }, []);

    const connect = () => {
        const socket = new SockJS('http://localhost:8080/chat-websocket');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: (frame) => {
                console.log('Connected: ' + frame);
                client.subscribe('/topic/public', (message) => {
                    if (message.body) {
                        setMessages((prevMessages) => [...prevMessages, JSON.parse(message.body)]);
                    }
                });
            },
            debug: (str) => {
                console.log(str);
            },
        });

        client.activate(); // Activate the client
        setStompClient(client); // Store the client in state for later use
    };

    const sendPublicMessage = () => {
        if (stompClient && stompClient.connected && messageContent && username) {
            const chatMessage = {
                sender: username,
                content: messageContent,
                type: "CHAT"
            };
            stompClient.publish({
                destination: "/app/publicMessage",
                body: JSON.stringify(chatMessage)
            });
            setMessageContent(''); // Clear input after sending
        }
    };

    const sendPrivateMessage = () => {
        if (stompClient && stompClient.connected && messageContent && username && recipient) {
            const chatMessage = {
                sender: username,
                recipient: recipient,
                content: messageContent,
                type: "PRIVATE"
            };
            stompClient.publish({
                destination: "/app/privateMessage",
                body: JSON.stringify(chatMessage)
            });
            setMessageContent(''); // Clear input after sending
        }
    };

    return (
        <div className="max-w-md mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Chat Application</h1>
            
            <input 
                type="text" 
                placeholder="Enter your name" 
                onChange={(e) => setUsername(e.target.value)} 
                className="border p-2 w-full mb-4"
            />

            <div className="border border-gray-300 h-64 overflow-y-scroll mb-4">
                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.sender}:</strong> {msg.content}
                    </div>
                ))}
            </div>

            <input 
                type="text" 
                value={messageContent} 
                onChange={(e) => setMessageContent(e.target.value)} 
                placeholder="Enter a message..." 
                className="border p-2 w-full mb-2"
            />
            
            <button onClick={sendPublicMessage} className="bg-blue-500 text-white p-2 mt-2 w-full">Send Public Message</button>

            <input 
                type="text" 
                placeholder="Recipient's name" 
                value={recipient} 
                onChange={(e) => setRecipient(e.target.value)} 
                className="border p-2 w-full mb-2 mt-4"
            />
            
            <button onClick={sendPrivateMessage} className="bg-green-500 text-white p-2 mt-2 w-full">Send Private Message</button>
        </div>
    );
};

export default Chat;