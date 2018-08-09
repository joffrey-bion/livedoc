// @flow
import './polyfills'
import 'bootstrap/dist/css/bootstrap.css';
import createHistory from 'history/createHashHistory';
import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'react-router-redux';
// $FlowFixMe: redux-persist is ignored in .flowconfig, so needs to be ignored here too
import { PersistGate } from 'redux-persist/es/integration/react';
import { App } from './App';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import configureStore from './store';

const history = createHistory();

async function bootstrap() {
  const {persistor, store} = configureStore(history);

  const rootElement = document.getElementById('root');
  if (!rootElement) {
    console.error('Element with ID "root" was not found, cannot bootstrap react app');
    return;
  }
  ReactDOM.render(<Provider store={store}>
    <PersistGate persistor={persistor}>
      <ConnectedRouter history={history}>
        <App/>
      </ConnectedRouter>
    </PersistGate>
  </Provider>, rootElement);

  registerServiceWorker();
}

bootstrap();
