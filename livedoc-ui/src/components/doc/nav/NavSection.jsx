// @flow
import * as React from 'react';
import { Panel } from 'react-bootstrap';
import type { Identified, Named } from '../../../model/livedoc';
import { NavGroup } from './NavGroup';

export type NavSectionProps = {
  title: string,
  elementsByGroupName: {[groupName: string]: $ReadOnlyArray<Identified & Named>},
  onSelect: (id: string) => void,
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

  return <Panel header={props.title} headerRole='button' collapsible>
      {navGroups}
  </Panel>;
};
