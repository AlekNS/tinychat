import * as lodash from 'lodash';
import React from 'react';
import PropTypes from 'prop-types';
import {FormGroup, InputGroup, FormControl, Button, Glyphicon} from 'react-bootstrap';
import {ChatService} from '../services';

/**
 * User message in chat.
 */
class MessageSend extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            messageToSend: ''
        };
        this.onKeyPress = this.onKeyPress.bind(this);
        this.onChangeMessage = this.onChangeMessage.bind(this);
        this.onSendMessage = this.onSendMessage.bind(this);
    }

    onKeyPress(event) {
        if (event.key == 'Enter') {
            this.onSendMessage();
        }
    }

    onChangeMessage(event) {
        this.setState({messageToSend: event.target.value});
    }

    onSendMessage() {
        const {selectedUsers} = this.props,
            {messageToSend} = this.state;
        if (messageToSend.trim()) {
            ChatService.send(
                messageToSend.trim(),
                lodash.chain(selectedUsers).pickBy((v) => !!v).keys().value()
            );
            this.setState({messageToSend: ''});
        }
    }

    render() {
        return <FormGroup>
            <InputGroup>
                <FormControl type="text"
                             value={this.state.messageToSend}
                             onChange={this.onChangeMessage}
                             onKeyPress={this.onKeyPress}/>
                <InputGroup.Button>
                    <Button bsStyle="primary" onClick={this.onSendMessage}><Glyphicon glyph="pencil"/> Send</Button>
                </InputGroup.Button>
            </InputGroup>
            <p className="help-block">Click on Send or press the enter, to send a message.</p>
        </FormGroup>;
    }
}

MessageSend.propTypes = {};

import {connect} from 'react-redux';

function mapStateToProps(state) {
    return {
        selectedUsers: state.users.selectedUsers
    };
}
export default connect(mapStateToProps)(MessageSend);
