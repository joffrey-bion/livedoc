// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Route, Switch, withRouter } from 'react-router-dom';
import type { Identified, Livedoc, Named } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { NavSection } from './NavSection';
import { GlobalNavSection } from './GlobalNavSection';

export type NavPanelProps = {
  livedoc: Livedoc,
}

const NavPanel = ({livedoc}: NavPanelProps) => {
  return <Switch>
    <Route path="/global" component={(props) => <GlobalNavSection globalDoc={livedoc.global} {...props}/>}/>
    <Route path="/apis" component={(props) => <NavSection groups={createGroups(livedoc.apis)} {...props}/>}/>
    <Route path="/types" component={(props) => <NavSection groups={createGroups(livedoc.objects)} {...props}/>}/>
    <Route path="/flows" component={(props) => <NavSection groups={createGroups(livedoc.flows)} {...props}/>}/>
  </Switch>;
};

function createGroups(elementsByGroupName: { [groupName: string]: $ReadOnlyArray<Identified & Named> }) {
  return Object.keys(elementsByGroupName).map(groupName => {
    const groupElements = elementsByGroupName[groupName].map(e => ({link: e.livedocId, name: e.name}));
    return {
      name: groupName,
      elements: groupElements,
    };
  });
}

const mapStateToProps = (state: State) => ({
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(NavPanel));
