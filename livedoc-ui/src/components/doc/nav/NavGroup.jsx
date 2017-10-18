// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import { NavItem, NavLink } from 'reactstrap';
import type { Identified, Named } from '../../../model/livedoc';
import './NavGroup.css';

export type NavGroupProps = {
  name: string,
  location: string,
  elements: $ReadOnlyArray<Identified & Named>,
}

export const NavGroup = (props: NavGroupProps) => {

  let items = [];

  if (props.name) {
    items.push(<h4>{props.name}</h4>)
  }

  const elements = props.elements.map(e => {
    return <NavItem key={e.livedocId}>
      <NavLink to={props.location + '/' + e.livedocId} tag={Link}>{e.name}</NavLink>
    </NavItem>;
  });

  return items.concat(elements);
};
