// @flow
import * as React from 'react';
import { Nav } from 'reactstrap';
import { NavGroup } from './NavGroup';
import type { NavGroupDescription } from './NavGroup';

export type NavSectionProps = {
  match: any,
  location: any,
  groups: NavGroupDescription[],
}

export const NavSection = (props: NavSectionProps) => {
  const navItems = props.groups.map((gp, index) => {
    return <NavGroup key={index} parentLoc={props.match.url} group={gp}/>
  });

  return <Nav vertical>{navItems}</Nav>
};
