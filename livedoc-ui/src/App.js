// @flow
import React, {Component} from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import './App.css';
import {InlineForm} from './components/forms/InlineForm';
import DocPresenter from './components/doc/DocPresenter';
import {connect} from 'react-redux';
import {actions} from './redux/reducer';

type Props = {
  fetchDoc: string => void;
}

class App extends Component<Props> {
  render() {
    return (<MuiThemeProvider>
      <div className="App">
        <div className="App-header">
          <h1 className="App-title">Livedoc</h1>
        </div>
        <InlineForm hintText='URL to JSON documentation' btnLabel='Get Doc'
                    initialValue={computeInitialUrl()}
                    onSubmit={this.props.fetchDoc}/>
        <DocPresenter />
      </div>
    </MuiThemeProvider>);
  }
}

function computeInitialUrl(): string {
  return window.location.href + 'jsondoc';
}

const mapStateToProps = state => ({
});

const mapDispatchToProps = {
  fetchDoc: actions.fetchDoc
};

export default connect(mapStateToProps, mapDispatchToProps)(App);
