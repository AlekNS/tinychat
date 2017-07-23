import * as lodash from 'lodash';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs/lib/stomp';
import { setUsers, receiveLastMessages, receiveMessages, setServerConnection } from './actions';

const CHAT_SOCKET_URL = process.env.NODE_ENV === 'development' ?
    'http://localhost:8080/chat-ws' :
    '/chat-ws'
;

/**
 * Inner WebSocket class to provide communication through STOMP.
 */
class WebSocketService {
    /**
     * Construct service and connect to the server.
     * @param url
     * @param username
     * @param password
     * @param reduxDispatch
     */
    constructor(url, username, password, reduxDispatch) {
        this.socketUrl = url;
        this.credentials = { username, password };
        this.reduxDispatch = reduxDispatch;
        this.init();
    }

    /**
     * Initialization
     */
    init() {
        this.disconnect();
        this.socket = new SockJS.default(this.socketUrl, {}, {});
        this.client = Stomp.Stomp.over(this.socket);
        this.client.connect(
            this.credentials.username,
            this.credentials.password,
            this.onConnect.bind(this),
            this.onError.bind(this)
        );
    }

    /**
     * Subscribe on all needed events
     */
    subscribe() {
        this.subsTopicUsers = this.client.subscribe('/topic/users', (users) => {
            this.onReceiveUsers(JSON.parse(users.body));
        });

        let lastMessageHandler = this.client.subscribe('/topic/messages/last', (messages) => {
            lastMessageHandler.unsubscribe();
            this.onReceiveLastMessages(JSON.parse(messages.body));
        });

        this.subsTopicMessages = this.client.subscribe('/topic/messages', (messages) => {
            this.onReceiveMessages([JSON.parse(messages.body)], false);
        });

        this.subsUserReply = this.client.subscribe('/user/queue/reply', (messages) => {
            this.onReceiveMessages([JSON.parse(messages.body)], true);
        });
    }

    /**
     * Request data.
     */
    retrieveChatData() {
        setTimeout(() => this.client.send('/chat/users', {}, ''), 10);
        setTimeout(() => this.client.send('/chat/messages/last', {}, ''), 20);
    }

    /**
     * Disconnect from server
     */
    disconnect() {
        if (this.client) {
            this.client.disconnect();
            this.client = null;
        }
    }

    /**
     * Connected event
     */
    onConnect() {
        this.reduxDispatch(setServerConnection('connected'));

        this.subscribe();
        this.retrieveChatData();
    }

    /**
     * Error event
     * @param err
     */
    onError(err) {
        (console.error || console.log)(err);
        this.reduxDispatch(setServerConnection('error'));
    }

    /**
     * Refresh user list event
     * @param users
     */
    onReceiveUsers(users) {
        this.reduxDispatch(setUsers(users));
    }

    /**
     * Refresh last messages (once execution)
     * @param messages
     */
    onReceiveLastMessages(messages) {
        this.reduxDispatch(receiveLastMessages(messages));
    }

    /**
     * Receive messages
     * @param messages
     * @param is_private
     */
    onReceiveMessages(messages, is_private) {
        this.reduxDispatch(receiveMessages(messages.map((message) => {
            return {...message, is_owner: message.from === this.credentials.username, is_private: !!is_private};
        })));
    }

    /**
     * Send public or private message
     * @param message
     * @param to Send private message to specified users (by user id)
     */
    sendMessage(message, to) {
        return this.client.send(
            "/chat/messages",
            {},
            JSON.stringify({ message: `${message}`, to: to && Array.isArray(to) && to.length ? to : [] })
        );
    }
}

/**
 * Simple wrapper
 */
export class ChatService {
    /**
     * First init
     * @param {string} username
     * @param {string} password
     * @param {object} reduxDispatch
     */
    static init(username, password, reduxDispatch) {
        this.service = new WebSocketService(CHAT_SOCKET_URL, username, password, reduxDispatch);
    }

    /**
     * Send messages
     * @param {string} message
     * @param {Array.<string>} toUsers
     */
    static send(message, toUsers) {
        this.service.sendMessage(message, toUsers);
    }
}
