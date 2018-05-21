// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Route, Switch, withRouter } from 'react-router-dom';
import type { Group, Identified, Livedoc, Named } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getLoadedDoc } from '../../../redux/doc';
import { GlobalNavSection } from './GlobalNavSection';
import type { NavGroupDescription } from './NavGroup';
import { NavSection } from './NavSection';

export type NavPanelProps = {
  livedoc: Livedoc,
}

const NavPanel = ({livedoc}: NavPanelProps) => {
  return <Switch>
    <Route path="/global" component={(props) => <GlobalNavSection globalDoc={livedoc.global} {...props}/>}/>
    <Route path="/apis" component={(props) => <NavSection groups={createNavGroups(livedoc.apis)} {...props}/>}/>
    <Route path="/types" component={(props) => <NavSection groups={createNavGroups(livedoc.types)} {...props}/>}/>
  </Switch>;
};

function createNavGroups<T : Identified & Named>(groups: Array<Group<T>>): Array<NavGroupDescription> {
  return groups.map(group => ({
    name: group.groupName,
    elements: group.elements.map(e => ({
      link: e.livedocId,
      name: e.name,
    })),
  }));
}

const mapStateToProps = (state: State) => ({
  livedoc: getLoadedDoc(state),
});

const mapDispatchToProps = {};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(NavPanel));
