// @flow
import * as React from 'react';
import { Nav, NavItem } from 'reactstrap';
import { NavLink } from 'react-router-dom';

export const TopNav = () => {
  return <Nav>
    <NavItem>
      <NavLink to="/global">Global</NavLink>
    </NavItem>
    <NavItem>
      <NavLink to="/apis">APIs</NavLink>
    </NavItem>
    <NavItem>
      <NavLink to="/types">Types</NavLink>
    </NavItem>
    <NavItem>
      <NavLink to="/flows">Flows</NavLink>
    </NavItem>
  </Nav>;
};
