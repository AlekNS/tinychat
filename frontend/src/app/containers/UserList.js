import React from 'react';
import PropTypes from 'prop-types';
import {ListGroup} from 'react-bootstrap';
import User from '../components/User';

/**
 * Display all users in chat.
 */
class UserList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedUsers: {}
        };
    }

    render() {
        const {users, selectedUsers, owner} = this.props;
        const usersCollection = users
            .filter((item) => owner.username != item.username)
            .map(
                (item) => <User isSelected={selectedUsers[item.username]}
                                username={item.username}
                                key={item.username}/>
            );

        return <div className="users-group"><ListGroup>{usersCollection}</ListGroup></div>;
    }
}

UserList.propTypes = {
    users: PropTypes.arrayOf(PropTypes.shape({
        username: PropTypes.string.isRequired
    }))
};

function mapStateToProps(state, ownProps) {
    return {
        owner: state.common.owner,
        users: ownProps.users,
        selectedUsers: state.users.selectedUsers
    };
}

import {connect} from 'react-redux';

export default connect(mapStateToProps)(UserList);
