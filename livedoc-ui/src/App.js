import React, {Component} from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import './App.css';
import {DocFetcher} from './components/DocFetcher';

class App extends Component {
  render() {
    return (<MuiThemeProvider>
      <div className="App">
        <div className="App-header">
          <h1 className="App-title">Livedoc</h1>
        </div>
        <DocFetcher fetch={fetch}/>
      </div>
    </MuiThemeProvider>);
  }
}

function fetch(url) {
  console.log(url);
}

export default App;
