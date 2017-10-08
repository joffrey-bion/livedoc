// @flow
import * as React from 'react';
import { Panel } from 'react-bootstrap';
import type { Identified, Named } from '../../../model/livedoc';
import { NavGroup } from './NavGroup';

type Props = {
  title: string,
  elementsByGroupName: {[key: string]: Array<Identified & Named>},
  onSelect: (id: string) => void,
}

export const NavSection = (props: Props) => {

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
