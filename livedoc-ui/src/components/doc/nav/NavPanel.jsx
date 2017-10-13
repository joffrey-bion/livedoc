// @flow
import * as React from 'react';
import { Nav } from 'reactstrap';
import type { Livedoc, LivedocID } from '../../../model/livedoc';
import { NavSection } from './NavSection';
import { connect } from 'react-redux';
import type { State } from '../../../model/state';
import { actions } from '../../../redux/reducer';

export type NavPanelProps = {
  livedoc: Livedoc,
  onSelect: (id: LivedocID) => void,
}

const NavPanel = ({livedoc, onSelect}: NavPanelProps) => {
  return <Nav card vertical>
    <h3>APIs</h3>
    <NavSection elementsByGroupName={livedoc.apis} onSelect={onSelect}/>
    <h3>Types</h3>
    <NavSection elementsByGroupName={livedoc.objects} onSelect={onSelect}/>
    <h3>Flows</h3>
    <NavSection elementsByGroupName={livedoc.flows} onSelect={onSelect}/>
  </Nav>;
};

const mapStateToProps = (state: State) => ({
  livedoc: state.livedoc,
});

const mapDispatchToProps = {
  onSelect: actions.selectElement,
};

export default connect(mapStateToProps, mapDispatchToProps)(NavPanel);
