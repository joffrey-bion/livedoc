// @flow
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import './App.css';
import DocPresenter from './components/doc/DocPresenter';
import { DocFetcher } from './components/fetcher/DocFetcher';
import Header from './components/header/Header';
import type { State } from './model/state';
import { isDocLoaded } from './redux/livedoc';
import { actions } from './redux/loader';

type Props = {
  jsonDocUrl: string, docLoaded: boolean, fetchDoc: string => void,
}

class App extends Component<Props> {
  render() {
    return (<MuiThemeProvider>
      <div className="App">
        <Header/>
        {!this.props.docLoaded && <DocFetcher fetchDoc={this.props.fetchDoc}/>}
        <DocPresenter/>
      </div>
    </MuiThemeProvider>);
  }
}

const mapStateToProps = (state: State) => ({
  jsonDocUrl: state.loader.url,
  docLoaded: isDocLoaded(state),
});

const mapDispatchToProps = {
  fetchDoc: actions.fetchDoc,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
