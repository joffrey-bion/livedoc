// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Route, Switch, withRouter } from 'react-router-dom';
import type { Livedoc } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { NavSection } from './NavSection';

export type NavPanelProps = {
  livedoc: Livedoc,
}

const NavPanel = ({livedoc}: NavPanelProps) => {
  return <Switch>
    <Route path="/apis" component={(props) => <NavSection elementsByGroupName={livedoc.apis} {...props}/>}/>
    <Route path="/types" component={(props) => <NavSection elementsByGroupName={livedoc.objects} {...props}/>}/>
    <Route path="/flows" component={(props) => <NavSection elementsByGroupName={livedoc.flows} {...props}/>}/>
    <Route exact path="/" component={(props) => <p>This is a test</p>}/>
  </Switch>;
};

const mapStateToProps = (state: State) => ({
  livedoc: state.livedoc,
});

const mapDispatchToProps = {};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(NavPanel));
