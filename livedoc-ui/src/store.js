// @flow
import {applyMiddleware, compose, createStore} from 'redux';
import createSagaMiddleware from 'redux-saga';
import reducer from './redux/reducer';
import rootSaga from './sagas';
import type { State } from './model/state';
import { newState } from './model/state';

export default function configureStore(initialState: State = newState()) {
  const sagaMiddleware = createSagaMiddleware();
  const middlewares = [sagaMiddleware];
  const enhancers = [applyMiddleware(...middlewares)];

  const composeEnhancers = process.env.NODE_ENV !== 'production' && typeof window === 'object' &&
  window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : compose;

  const store = createStore(reducer, initialState, composeEnhancers(...enhancers));

  sagaMiddleware.run(rootSaga);

  return store;
}
