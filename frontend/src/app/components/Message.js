import React from 'react';
import PropTypes from 'prop-types';
import {Row, Col, Panel, Glyphicon} from 'react-bootstrap';

import {selectUser} from '../actions';

/**
 * User message in chat.
 */
const Message = ({item, selectUser}) => {
    const {from, stamp, message, is_owner, is_private} = item;
    const header = <div className="caption" onClick={() => selectUser(item.from, true)}>
        {from ? from : ''}
        {' '} {stamp ? '(' + stamp + ')' : ''}
        {' '} {is_private ? <Glyphicon glyph="lock"/> : ''}
    </div>;


    return <Col sm={12}>
        <Col sm={10} className={ is_owner ? 'col-sm-push-1' : '' }>
            <Panel header={is_owner ? '' : header}
                   bsStyle={!is_private ? 'info' : 'danger'}>
                {message}
            </Panel>
        </Col>
    </Col>;
};

Message.propTypes = {
    item: PropTypes.object.isRequired
};

import {connect} from 'react-redux';

export default connect(null, {selectUser})(Message);
