// @flow
import React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter } from 'react-router-dom';
import { Doc } from './components/doc/DocPresenter';
import { DocFetcher } from './components/fetcher/DocFetcher';
import { LoadingSpinner } from './components/shared/LoadingSpinner';
import { Header } from './components/header/Header';
import type { State } from './model/state';
import type { LoadingState } from './redux/loader';
import { getLoadingState } from './redux/loader';

export const APP_VERSION = process.env.REACT_APP_VERSION || '?:?:?';

type AppProps = {
  loadingState: LoadingState,
}

const AppPresenter = (props: AppProps) => (<div>
  <Header/>
  <AppContent {...props}/>
</div>);

const AppContent = ({loadingState}: AppProps) => {
  if (loadingState === 'LOADING_NEW') {
    return <LoadingSpinner/>;
  }
  return <Switch>
    <Route path="/fetch" render={() => <DocFetcher/>}/>
    {loadingState === 'IDLE_EMPTY' && <Redirect to="/fetch"/>}
    <Route path="/" component={Doc}/>
  </Switch>;
};

const mapStateToProps = (state: State): AppProps => ({
  loadingState: getLoadingState(state),
});

const mapDispatchToProps = {};

export const App = withRouter(connect(mapStateToProps, mapDispatchToProps)(AppPresenter));
