// @flow
import * as React from 'react';
import type { Livedoc } from '../../../model/livedoc';
import { NavSection } from './NavSection';

type Props = {
  livedoc: Livedoc,
  onSelect: (id: string) => void,
}

export const NavPanel = ({livedoc, onSelect}: Props) => {
  return <nav>
    <NavSection title={'APIs'} elementsByGroupName={livedoc.apis} onSelect={onSelect}/>
    <NavSection title={'Types'} elementsByGroupName={livedoc.objects} onSelect={onSelect}/>
    <NavSection title={'Flows'} elementsByGroupName={livedoc.flows} onSelect={onSelect}/>
  </nav>;
};
