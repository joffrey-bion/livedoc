// @flow
import * as React from 'react';
import type { Identified, Named } from '../../../models/livedoc';
import { NavGroup } from './NavGroup';

type Props = {
  title: string,
  elementsByGroupName: {[key: string]: Array<Identified & Named>},
  onSelect: (id: string) => void,
}

export const NavSection = (props: Props) => {

  let navGroups = [];
  for (let key in props.elementsByGroupName) {
    const groupName = key;
    const elements: Array<Identified & Named> = props.elementsByGroupName[key];
    navGroups.push(<NavGroup key={groupName} title={groupName} elements={elements} onSelect={props.onSelect}/>);
  }

  return <div>
    <h2>{props.title}</h2>
    <div style={{border: '1px'}}>
      {navGroups}
    </div>
  </div>;
};

