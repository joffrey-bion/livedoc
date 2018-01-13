// @flow
import * as React from 'react';
import { NavLink as RouterNavLink } from 'react-router-dom';
import { NavItem, NavLink } from 'reactstrap';
import './NavGroup.css';

export type NavElementDescription = {
  link: string,
  name: string,
  disabled?: boolean,
}

export type NavGroupDescription = {
  name?: string,
  elements: NavElementDescription[],
}

export type NavGroupProps = {
  group: NavGroupDescription,
  parentLoc: string,
}

export const NavGroup = (props: NavGroupProps) => {
  let items = [];
  if (props.group.name) {
    items.push(<div key="-1" className="group-title">{props.group.name}</div>);
  }

  const elements = props.group.elements.map((e, index) => {
    return <NavItem key={index}>
      <NavLink className="nav-anchor"
               to={props.parentLoc + '/' + e.link}
               disabled={e.disabled}
               tag={RouterNavLink}>{e.name}</NavLink>
    </NavItem>;
  });

  return items.concat(elements);
};
