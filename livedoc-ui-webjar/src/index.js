// @flow
import 'bootstrap/dist/css/bootstrap.css';
import createHistory from 'history/createHashHistory';
import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'react-router-redux';
import { PersistGate } from 'redux-persist/es/integration/react';
import App from './App';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import configureStore from './store';

const history = createHistory();

async function bootstrap() {
  const {persistor, store} = configureStore(history);

  ReactDOM.render(<Provider store={store}>
    <PersistGate persistor={persistor}>
      <ConnectedRouter history={history}>
        <App/>
      </ConnectedRouter>
    </PersistGate>
  </Provider>, document.getElementById('root'));

  registerServiceWorker();
}

bootstrap();
