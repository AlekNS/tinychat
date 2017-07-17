import * as lodash from 'lodash';
import {combineReducers} from 'redux';
import {
    SET_OWNER, SET_SERVER_CONNECTION,
    SET_USERS, SELECT_USER, DESELECT_ALL_USERS,
    RECEIVE_LAST_MESSAGES, RECEIVE_MESSAGES
} from './actions';

export function commonReducer(state = {}, action) {
    switch (action.type) {
        case SET_OWNER:
            return {
                ...state,
                owner: action.owner
            };
        case SET_SERVER_CONNECTION:
            return {
                ...state,
                connection: action.connection
            };
        default:
            return state;
    }
}

function syncUsersAndSelectedUsers(users, selectedUsers) {
    return lodash.pick(selectedUsers,
        lodash.chain(Object.keys(selectedUsers))
            .filter((username) => lodash.findIndex(users, {username}) > -1)
            .value()
    );
}

export function usersReducer(state = {}, action) {
    switch (action.type) {
        case SET_USERS:
            return {
                ...state,
                users: action.users,
                selectedUsers: syncUsersAndSelectedUsers(
                    action.users,
                    state.selectedUsers
                )
            };
        case SELECT_USER:
            return {
                ...state,
                selectedUsers: syncUsersAndSelectedUsers(
                    state.users,
                    {...state.selectedUsers, ...action.selectedUsers}
                )
            };
        case DESELECT_ALL_USERS:
            return {
                ...state,
                selectedUsers: {}
            };
        default:
            return state;
    }
}

export function chatReducer(state = {}, action) {
    switch (action.type) {
        case RECEIVE_LAST_MESSAGES:
            return {
                ...state,
                messages: action.messages.concat(state.messages)
            };
        case RECEIVE_MESSAGES:
            console.log(action, action.messages)
            return {
                ...state,
                messages: state.messages.concat(action.messages)
            };
        default:
            return state;
    }
}

export default combineReducers({
    common: commonReducer,
    users: usersReducer,
    chat: chatReducer
});
