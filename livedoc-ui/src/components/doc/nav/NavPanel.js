// @flow
import * as React from 'react';
import type {Livedoc} from '../../../models/livedoc';
import { NavSection } from './NavSection';

type Props = {
  livedoc: Livedoc,
  onSelect: (id: string) => void,
}

export const NavPanel = (props: Props) => {
  const livedoc: Livedoc = props.livedoc;
  return <nav>
    <NavSection title={'APIs'} elementsByGroupName={livedoc.apis} onSelect={props.onSelect}/>
    <NavSection title={'Types'} elementsByGroupName={livedoc.objects} onSelect={props.onSelect}/>
    <NavSection title={'Flows'} elementsByGroupName={livedoc.flows} onSelect={props.onSelect}/>
  </nav>
};

