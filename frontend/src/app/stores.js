import {applyMiddleware, createStore} from 'redux';
import {createLogger} from 'redux-logger';
import reactThunk from 'redux-thunk';

import rootReducer from './reducers';

let middlewares = [reactThunk];

if (process.env.NODE_ENV !== 'production') {
    middlewares.push(createLogger());
}

export const applicationStore = createStore(
    rootReducer,
    {
        common: {
            connection: 'unknown', // connection status
            owner: {} // current user
        },
        users: {
            users: [], // users in chat
            selectedUsers: {} // to send private messages
        },
        chat: {
            messages: [] // current chat messages
        }
    },
    applyMiddleware(...middlewares)
);

import {connectToServer, setRandomOwnerName} from './actions';
applicationStore.dispatch(setRandomOwnerName());
applicationStore.dispatch(connectToServer(applicationStore.getState().common.owner));
