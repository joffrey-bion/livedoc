// @flow
import * as React from 'react';
import { Nav, NavItem } from 'react-bootstrap';
import type { Identified, LivedocID, Named } from '../../../model/livedoc';
import './NavGroup.css';

export type NavGroupProps = {
  name: string,
  elements: $ReadOnlyArray<Identified & Named>,
  onSelect: (id: LivedocID) => void,
}

export const NavGroup = (props: NavGroupProps) => {

  const navLinks = props.elements.map(e => <NavItem key={e.livedocId} eventKey={e.livedocId}>{e.name}</NavItem>);

  const navGroup = <Nav bsStyle="pills" stacked onSelect={props.onSelect}>
    {navLinks}
  </Nav>;

  if (props.name) {
    return <div>
      <h4>{props.name}</h4>
      {navGroup}
    </div>;
  } else {
    return navGroup;
  }
};
