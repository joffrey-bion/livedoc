// @flow
import * as React from 'react';
import type { Identified, LivedocID, Named } from '../../../model/livedoc';
import { NavGroup } from './NavGroup';

export type NavSectionProps = {
  elementsByGroupName: { [groupName: string]: $ReadOnlyArray<Identified & Named> },
  onSelect: (id: LivedocID) => void,
}

export const NavSection = (props: NavSectionProps) => {

  let navGroups = [];
  for (let key in props.elementsByGroupName) {
    if (props.elementsByGroupName.hasOwnProperty(key)) {
      const groupName = key;
      const elements = props.elementsByGroupName[key];
      navGroups.push(<NavGroup key={groupName} name={groupName} elements={elements} onSelect={props.onSelect}/>);
    }
  }
  return navGroups;
};
