// @flow
import { routerMiddleware as createRouterMiddleware, routerReducer } from 'react-router-redux';
import { applyMiddleware, compose, createStore } from 'redux';
import { persistCombineReducers, persistStore } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import createSagaMiddleware from 'redux-saga';
import type { State } from './model/state';
import { newState } from './model/state';
import livedocReducer from './redux/livedoc';
import loaderReducer from './redux/loader';
import playgroundReducer from './redux/playground';
import rootSaga from './sagas';

const persistConfig = {
  key: 'livedoc',
  storage,
  whitelist: ['livedoc'],
};

export default function configureStore(history: any, initialState: State = newState()) {
  const sagaMiddleware = createSagaMiddleware();
  const routerMiddleware = createRouterMiddleware(history);
  const middlewares = [sagaMiddleware, routerMiddleware];
  const enhancers = [applyMiddleware(...middlewares)];

  const composeEnhancers = process.env.NODE_ENV !== 'production' && typeof window === 'object' &&
  window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : compose;

  const reducers = persistCombineReducers(persistConfig, {
    livedoc: livedocReducer,
    loader: loaderReducer,
    playground: playgroundReducer,
    router: routerReducer,
  });
  const store = createStore(reducers, initialState, composeEnhancers(...enhancers));
  const persistor = persistStore(store);

  sagaMiddleware.run(rootSaga);

  return {
    persistor,
    store,
  };
}
