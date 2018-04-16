// @flow
import React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter } from 'react-router-dom';
import { Doc } from './components/doc/DocPresenter';
import { DocFetcher } from './components/fetcher/DocFetcher';
import { Header } from './components/header/Header';
import type { State } from './model/state';
import { isDocLoaded } from './redux/livedoc';

export const APP_VERSION = process.env.REACT_APP_VERSION || '?:?:?';

type Props = {
  loading: boolean,
  url: ?string,
  docLoaded: boolean,
}

const AppPresenter = (props: Props) => (<div>
          <Header/>
          <Switch>
            <Route path="/fetch" render={() => <DocFetcher/>}/>
            {!props.docLoaded && <Redirect to="/fetch"/>}
            <Route path="/" component={Doc}/>
          </Switch>
        </div>);

const mapStateToProps = (state: State) => ({
  docLoaded: isDocLoaded(state),
});

const mapDispatchToProps = {};

export const App = withRouter(connect(mapStateToProps, mapDispatchToProps)(AppPresenter));
