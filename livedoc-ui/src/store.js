// @flow
import { routerMiddleware as createRouterMiddleware, routerReducer } from 'react-router-redux'
import { applyMiddleware, combineReducers, compose, createStore } from 'redux';
import createSagaMiddleware from 'redux-saga';
import type { State } from './model/state';
import { newState } from './model/state';
import rootSaga from './sagas';
import loaderReducer from './redux/loader';
import livedocReducer from './redux/livedoc';

export default function configureStore(history: any, initialState: State = newState()) {
  const sagaMiddleware = createSagaMiddleware();
  const routerMiddleware = createRouterMiddleware(history);
  const middlewares = [sagaMiddleware, routerMiddleware];
  const enhancers = [applyMiddleware(...middlewares)];

  const composeEnhancers = process.env.NODE_ENV !== 'production' && typeof window === 'object' &&
  window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : compose;

  const reducers = combineReducers({
    livedoc: livedocReducer,
    loader: loaderReducer,
    router: routerReducer,
  });
  const store = createStore(reducers, initialState, composeEnhancers(...enhancers));

  sagaMiddleware.run(rootSaga);

  return store;
}
