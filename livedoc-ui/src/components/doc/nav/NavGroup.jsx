// @flow
import * as React from 'react';
import { NavItem } from 'reactstrap';
import type { Identified, LivedocID, Named } from '../../../model/livedoc';
import './NavGroup.css';

export type NavGroupProps = {
  name: string,
  elements: $ReadOnlyArray<Identified & Named>,
  onSelect: (id: LivedocID) => void,
}

export const NavGroup = (props: NavGroupProps) => {

  let items = [];

  if (props.name) {
    items.push(<h4>{props.name}</h4>)
  }

  const elements = props.elements.map(e => {
    return <NavItem key={e.livedocId} onClick={() => props.onSelect(e.livedocId)}>{e.name}</NavItem>;
  });

  return items.concat(elements);
};
