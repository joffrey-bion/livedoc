// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter } from 'react-router-dom';
import type { ApiGlobalDoc } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getGlobalDoc } from '../../../redux/doc';
import { RouteHelper } from '../../../routing/routeHelper';
import { GlobalDocPage } from './GlobalDocPage';

export type GlobalDocPanelProps = {
  globalDoc: ApiGlobalDoc,
}

const GlobalDocPanelPresenter = ({globalDoc}: GlobalDocPanelProps) => {
  const routes = globalDoc.pages.map(page => {
    let pageUrl = RouteHelper.globalPageUrl(page.livedocId);
    return <Route key={page.livedocId} path={pageUrl} render={() => <GlobalDocPage content={page.content}/>}/>
  });
  return <Switch>
    {routes}
    <Redirect to={RouteHelper.globalPageUrl(globalDoc.homePageId)}/>
  </Switch>;
};

const mapStateToProps = (state: State) => ({
  globalDoc: getGlobalDoc(state),
});

const mapDispatchToProps = {};

export const GlobalDocPanel = withRouter(connect(mapStateToProps, mapDispatchToProps)(GlobalDocPanelPresenter));


