// @flow
import * as React from 'react';
import type { Identified, LivedocID, Named } from '../../../model/livedoc';
import { NavGroup } from './NavGroup';

export type NavSectionProps = {
  elementsByGroupName: { [groupName: string]: $ReadOnlyArray<Identified & Named> },
  onSelect: (id: LivedocID) => void,
}

export const NavSection = (props: NavSectionProps) => {

  return Object.entries(props.elementsByGroupName).map(([gpName, elems]) => {
    return <NavGroup key={gpName || 'no-group'} name={gpName} elements={elems} onSelect={props.onSelect}/>
  });
};
