// @flow
import * as React from 'react';
import { NavLink as RouterNavLink } from 'react-router-dom';
import { Nav, NavItem, NavLink } from 'reactstrap';
import './TopNav.css';

const TopNavItem = ({path, name}) => (<NavItem>
  <NavLink className="top-nav-anchor" to={path} tag={RouterNavLink}>{name}</NavLink>
</NavItem>);

export const TopNav = () => {
  return <Nav className="mr-auto">
    <TopNavItem path="/global" name='Global'/>
    <TopNavItem path="/apis" name='APIs'/>
    <TopNavItem path="/types" name='Types'/>
    <TopNavItem path="/flows" name='Flows'/>
  </Nav>;
};
