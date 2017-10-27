// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import { Nav, NavItem, NavLink } from 'reactstrap';

export type GlobalNavSectionProps = {
  match: any,
}

export const GlobalNavSection = (props: GlobalNavSectionProps) => {
  return <Nav vertical>
    <NavItem>
      <NavLink to={props.match.url + '/general'} tag={Link}>General</NavLink>
    </NavItem>
    <NavItem>
      <NavLink to={props.match.url + '/changelog'} tag={Link}>Change Log</NavLink>
    </NavItem>
    <NavItem>
      <NavLink to={props.match.url + '/migrations'} tag={Link}>Migrations</NavLink>
    </NavItem>
  </Nav>;
};
