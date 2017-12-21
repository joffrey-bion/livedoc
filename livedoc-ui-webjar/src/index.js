// @flow
import 'bootstrap/dist/css/bootstrap.css';
import createHistory from 'history/createHashHistory';
import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'react-router-redux';
import { createPersistor, getStoredState } from 'redux-persist';
import App from './App';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import configureStore from './store';

const history = createHistory();
const persistConfig = {};

async function bootstrap() {
  const restoredState = await getStoredState(persistConfig);
  const store = configureStore(history, restoredState);
  createPersistor(store, persistConfig);

  ReactDOM.render(<Provider store={store}>
    <ConnectedRouter history={history}>
      <App/>
    </ConnectedRouter>
  </Provider>, document.getElementById('root'));
  registerServiceWorker();
}

bootstrap();
