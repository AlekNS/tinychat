import React from 'react';
import PropTypes from 'prop-types';
import {ListGroupItem, Glyphicon} from 'react-bootstrap';

import {selectUser} from '../actions';

/**
 * User item component.
 */
class User extends React.Component {
    constructor(props) {
        super(props);
        this.onSelectUser = this.onSelectUser.bind(this);
    }

    onSelectUser(event) {
        const {username, isSelected} = this.props;
        if (event.type == 'click') {
            this.props.selectUser(username, !isSelected);
        }
    }

    render() {
        const {username, isSelected} = this.props,
            selectedIcon = isSelected ? <Glyphicon className="pull-right" glyph="ok"/> : '';

        return <ListGroupItem onClick={this.onSelectUser} active={isSelected}>
            <Glyphicon glyph="user"/> {username} {selectedIcon}
        </ListGroupItem>;
    }
}

User.propTypes = {
    username: PropTypes.string.isRequired
};

import {connect} from 'react-redux';

export default connect(null, {selectUser})(User);
