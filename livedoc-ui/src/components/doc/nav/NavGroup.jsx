// @flow
import * as React from 'react';
import type { Identified, Named } from '../../../model/livedoc';

type Props = {
  title: string,
  elements: $ReadOnlyArray<Identified & Named>,
  onSelect: (id: string) => void,
}

export const NavGroup = (props: Props) => {

  const navLinks = props.elements.map(elt => <li key={elt.livedocId}>
    <a href="#" onClick={e => props.onSelect(elt.livedocId)}>{elt.name}</a>
  </li>);

  return <div>
    <h2>{props.title}</h2>
    <ul>
      {navLinks}
    </ul>
  </div>;
};

