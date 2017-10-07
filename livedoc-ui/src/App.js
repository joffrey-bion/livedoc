// @flow
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import './App.css';
import DocPresenter from './components/doc/DocPresenter';
import { DocFetcher } from './components/fetcher/DocFetcher';
import { Header } from './components/header/Header';
import { actions } from './redux/reducer';

type Props = {
  fetchDoc: string => void;
}

class App extends Component<Props> {
  render() {
    return (<MuiThemeProvider>
      <div className="App">
        <Header/>
        <DocFetcher fetchDoc={this.props.fetchDoc}/>
        <DocPresenter/>
      </div>
    </MuiThemeProvider>);
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = {
  fetchDoc: actions.fetchDoc,
};

export default connect(mapStateToProps, mapDispatchToProps)(App);
