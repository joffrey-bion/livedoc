// @flow
import * as React from 'react';
import { Nav } from 'reactstrap';
import type { Identified, Named } from '../../../model/livedoc';
import { NavGroup } from './NavGroup';

export type NavSectionProps = {
  match: any,
  elementsByGroupName: { [groupName: string]: $ReadOnlyArray<Identified & Named> },
}

export const NavSection = (props: NavSectionProps) => {

  const navItems = Object.entries(props.elementsByGroupName).map(([gpName, elems]) => {
    return <NavGroup key={gpName || 'no-group'} name={gpName} elements={elems} location={props.match.url}/>
  });

  return <Nav vertical>{navItems}</Nav>
};
