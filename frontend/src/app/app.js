import {Provider} from 'react-redux';
import React from 'react';
import ReactDOM from 'react-dom';

import {applicationStore} from './stores';
import Application from './containers/Application';

ReactDOM.render(
    <Provider store={applicationStore}>
        <Application/>
    </Provider>,
    document.getElementById('application')
);
