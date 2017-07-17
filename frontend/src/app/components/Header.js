import React from 'react';
import {Navbar, Nav, NavItem} from 'react-bootstrap';
import PropTypes from 'prop-types';

/**
 * Header Component.
 */
const Header = ({userName}) => {
    return <Navbar>
        <Navbar.Header>
            <Navbar.Brand><a href="#" onClick={(e) => e.preventDefault()}>TinyChat</a></Navbar.Brand>
        </Navbar.Header>
        <Navbar.Collapse>
            <Navbar.Text>You are {userName || 'Unknown!?'}</Navbar.Text>
        </Navbar.Collapse>
    </Navbar>;
};

Header.propTypes = {
    userName: PropTypes.string
};

export default Header;
