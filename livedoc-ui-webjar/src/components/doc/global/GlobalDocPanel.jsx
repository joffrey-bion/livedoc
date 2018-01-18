// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter } from 'react-router-dom';
import type { ApiGlobalDoc } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getGlobalDoc } from '../../../redux/livedoc';
import { ChangeLogs } from './changelog/ChangeLogs';
import { Migrations } from './migrations/Migrations';
import { GlobalDocSections } from './sections/GlobalDocSections';

export type GlobalDocPanelProps = {
  globalDoc: ApiGlobalDoc,
}

const GlobalDocPanelPresenter = ({globalDoc}: GlobalDocPanelProps) => {
  return <Switch>
    <Route path="/global/general" render={() => <GlobalDocSections sections={globalDoc.sections}/>}/>
    <Route path="/global/changelog" render={() => <ChangeLogs changeLogs={globalDoc.changelogset}/>}/>
    <Route path="/global/migrations" render={() => <Migrations migrations={globalDoc.migrationset}/>}/>
    <Redirect to="/global/general"/>
  </Switch>;
};

const mapStateToProps = (state: State) => ({
  globalDoc: getGlobalDoc(state),
});

const mapDispatchToProps = {};

export const GlobalDocPanel = withRouter(connect(mapStateToProps, mapDispatchToProps)(GlobalDocPanelPresenter));


