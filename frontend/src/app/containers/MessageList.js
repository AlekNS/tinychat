import React from 'react';
import ReactDOM from 'react-dom';
import PropTypes from 'prop-types';
import Message from '../components/Message';

/**
 * Container of user messages.
 */
class MessageList extends React.Component {
    scrollToLastMessage() {
        this.element = this.element || ReactDOM.findDOMNode(this.refs.element);
        if (this.element) {
            this.element.scrollTop = this.element.scrollHeight - this.element.clientHeight;
        }
    }

    componentDidMount() {
        this.scrollToLastMessage();
    }

    componentDidUpdate() {
        this.scrollToLastMessage();
    }

    render() {
        return <div ref="element" className="messages">
            {this.props.messages.map((item) => <Message key={item.id} item={item}/>)}
        </div>;
    }
}

MessageList.propTypes = {
    messages: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.string.isRequired,
        message: PropTypes.string.isRequired
    }))
};

import {connect} from 'react-redux';

export default MessageList;
