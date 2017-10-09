// @flow
import * as React from 'react';
import { PanelGroup } from 'react-bootstrap';
import type { Livedoc } from '../../../model/livedoc';
import { NavSection } from './NavSection';
import { connect } from 'react-redux';
import type { State } from '../../../model/state';
import { actions } from '../../../redux/reducer';

export type NavPanelProps = {
  livedoc: Livedoc,
  onSelect: (id: string) => void,
}

const NavPanel = ({livedoc, onSelect}: NavPanelProps) => {
  return <PanelGroup role='navigation' accordion>
    <NavSection title={'APIs'} elementsByGroupName={livedoc.apis} onSelect={onSelect}/>
    <NavSection title={'Types'} elementsByGroupName={livedoc.objects} onSelect={onSelect}/>
    <NavSection title={'Flows'} elementsByGroupName={livedoc.flows} onSelect={onSelect}/>
  </PanelGroup>;
};

const mapStateToProps = (state: State) => ({
  livedoc: state.livedoc,
});

const mapDispatchToProps = {
  onSelect: actions.selectElement,
};

export default connect(mapStateToProps, mapDispatchToProps)(NavPanel);
