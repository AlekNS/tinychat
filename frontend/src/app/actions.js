export const SET_OWNER = 'SET_OWNER';
export const SET_SERVER_CONNECTION = 'SET_SERVER_CONNECTION';

export const SET_USERS = 'SET_USERS';
export const SELECT_USER = 'SELECT_USER';
export const DESELECT_ALL_USERS = 'DESELECT_ALL_USERS';

export const RECEIVE_LAST_MESSAGES = 'RECEIVE_LAST_MESSAGES';
export const RECEIVE_MESSAGES = 'RECEIVE_MESSAGES';

import { ChatService } from './services';



export function connectToServer(owner) {
    return (dispatch) => {
        ChatService.init(owner.username, null, dispatch);
    };
}

//
// Common actions
//
export const setRandomOwnerName = () => setOwner({username: 'user_' + ~~(Math.random() * 1e8 + (+new Date))});

/**
 * @param {Object} owner
 */
export const setOwner = (owner) => ({
    type: SET_OWNER,
    owner: owner
});

/**
 * @param {string} connection
 */
export const setServerConnection = (connection) => ({
    type: SET_SERVER_CONNECTION,
    connection
});


//
// Users action
//
// @TODO: add User class
/**
 * @param {Array.<object>} users
 */
export const setUsers = (users) => ({
    type: SET_USERS,
    users: users
});

/**
 * @param {string} userId
 * @param {boolean} isSelected
 */
export const selectUser = (userId, isSelected) => ({
    type: SELECT_USER,
    selectedUsers: {[userId]: isSelected}
});

export const deselectAllUsers = () => ({
    type: DESELECT_ALL_USERS
});


//
// Chat message actions
//
// @TODO: add Message class
/**
 * @param {Array.<object>} messages
 */
export const receiveLastMessages = (messages) => ({
    type: RECEIVE_LAST_MESSAGES,
    messages
});

/**
 * @param {Array.<object>} messages
 */
export const receiveMessages = (messages) => ({
    type: RECEIVE_MESSAGES,
    messages
});
