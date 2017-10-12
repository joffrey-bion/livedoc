// @flow
import * as React from 'react';
import { Panel, PanelGroup } from 'react-bootstrap';
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
  return <PanelGroup role='navigation' defaultActiveKey='0' accordion>
    <Panel header='APIs' headerRole='button' eventKey='0' collapsible>
      <NavSection elementsByGroupName={livedoc.apis} onSelect={onSelect}/>
    </Panel>
    <Panel header='Types' headerRole='button' eventKey='1' collapsible>
      <NavSection elementsByGroupName={livedoc.objects} onSelect={onSelect}/>
    </Panel>
    <Panel header='Flows' headerRole='button' eventKey='2' collapsible>
      <NavSection elementsByGroupName={livedoc.flows} onSelect={onSelect}/>
    </Panel>
  </PanelGroup>;
};

const mapStateToProps = (state: State) => ({
  livedoc: state.livedoc,
});

const mapDispatchToProps = {
  onSelect: actions.selectElement,
};

export default connect(mapStateToProps, mapDispatchToProps)(NavPanel);
