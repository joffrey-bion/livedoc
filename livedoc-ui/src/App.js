// @flow
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter } from 'react-router-dom';
import './App.css';
import DocPresenter from './components/doc/DocPresenter';
import { DocFetcher } from './components/fetcher/DocFetcher';
import Header from './components/header/Header';
import type { State } from './model/state';
import { isDocLoaded } from './redux/livedoc';
import { actions } from './redux/loader';

type Props = {
  docLoaded: boolean,
  fetchDoc: string => void,
}

class App extends Component<Props> {
  render() {
    return (<MuiThemeProvider>
      <div className="App">
        <Header/>
        <Switch>
          <Route path="/fetch" render={(props) => <DocFetcher fetchDoc={this.props.fetchDoc}/>}/>
          {!this.props.docLoaded &&  <Redirect to="/fetch"/>}
          <Route path="/" component={DocPresenter}/>
        </Switch>
      </div>
    </MuiThemeProvider>);
  }
}

const mapStateToProps = (state: State) => ({
  docLoaded: isDocLoaded(state),
});

const mapDispatchToProps = {
  fetchDoc: actions.fetchDoc,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
