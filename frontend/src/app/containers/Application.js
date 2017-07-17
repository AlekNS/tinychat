import React from 'react';
import {Grid, Row, Col, Panel, ProgressBar} from 'react-bootstrap';
import Header from '../components/Header';
import UserList from './UserList';
import UsersControl from '../components/UsersControl';
import MessageList from './MessageList';
import MessageSend from '../components/MessageSend';

/**
 * Main application component.
 */
const Application = ({connection, owner, users, messages}) => {
    const isConnected = connection == 'connected',
        isError = connection == 'error';

    return <Grid>
        <Header userName={owner.username}/>
        {isConnected ? // Displaying normal chat
            <Row>
                <Col sm={4}>
                    <Panel header={"Users"} bsStyle="primary">
                        <UsersControl />
                        <br />
                        <UserList users={users}/>
                    </Panel>
                </Col>
                <Col sm={8}>
                    <Panel header={"Messages"} bsStyle="primary">
                        <MessageList messages={messages}/>
                        <br />
                        <MessageSend />
                    </Panel>
                </Col>
            </Row> :
            ( // Something other instead successful connection
                <Row className="text-center">
                    {isError ? // On error
                        <Col sm={12}>
                            <p>Error was occurred when connecting to the server.</p>
                        </Col>
                        : // On connecting
                        <Col sm={12}>
                            Connecting to the server...
                            <ProgressBar active now={100}/>
                        </Col>
                    }
                </Row>
            )
        }
    </Grid>;
};

function mapStateToProps(state) {
    return {
        owner: state.common.owner,
        connection: state.common.connection,
        messages: state.chat.messages,
        users: state.users.users
    };
}

import {connect} from 'react-redux';

export default connect(mapStateToProps)(Application);
