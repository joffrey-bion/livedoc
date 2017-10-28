// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
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
  currentLoc: string,
  parentLoc: string,
}

export const NavGroup = (props: NavGroupProps) => {

  let items = [];

  if (props.group.name) {
    items.push(<span className="groupTitle">{props.group.name}</span>)
  }

  const elements = props.group.elements.map((e, index) => {
    const linkUrl = props.parentLoc + '/' + e.link;
    const active = props.currentLoc === linkUrl;
    return <NavItem key={index}>
      <NavLink className="nav-anchor"
               to={linkUrl}
               disabled={e.disabled}
               active={active}
               tag={Link}>{e.name}</NavLink>
    </NavItem>;
  });

  return items.concat(elements);
};
