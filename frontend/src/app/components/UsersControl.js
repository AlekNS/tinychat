import React from 'react';
import PropTypes from 'prop-types';
import {Button, Glyphicon} from 'react-bootstrap';

import {deselectAllUsers} from '../actions';

/**
 * User item component.
 */
class UsersControl extends React.Component {
    constructor(props) {
        super(props);
        this.onDeselectAllUsers = this.onDeselectAllUsers.bind(this);
    }

    onDeselectAllUsers() {
        this.props.deselectAllUsers();
    }

    render() {
        return <div>
            <p className="help-block">Click on user(s) to send a private message.</p>
            <div>
                <Button bsStyle="warning"
                        bsSize="small"
                        onClick={this.onDeselectAllUsers}
                        className="deselect-all-users">
                    <Glyphicon glyph="remove"/> Deselect
                </Button>
            </div>
        </div>;
    }
}

UsersControl.propTypes = {};

import {connect} from 'react-redux';

export default connect(null, {deselectAllUsers})(UsersControl);
