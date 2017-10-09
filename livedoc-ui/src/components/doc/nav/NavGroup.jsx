// @flow
import * as React from 'react';
import { Nav, NavItem, Panel } from 'react-bootstrap';
import type { Identified, Named } from '../../../model/livedoc';

export type NavGroupProps = {
  name: string,
  elements: $ReadOnlyArray<Identified & Named>,
  onSelect: (id: string) => void,
}

export const NavGroup = (props: NavGroupProps) => {

  const navLinks = props.elements.map(e => <NavItem key={e.livedocId} eventKey={e.livedocId}>{e.name}</NavItem>);

  const navGroup = <Nav bsStyle="pills" stacked onSelect={props.onSelect}>
    {navLinks}
  </Nav>;

  if (props.name) {
    return <Panel header={props.name} headerRole='button' collapsible>
      {navGroup}
    </Panel>;
  } else {
    return navGroup;
  }
};
