import { fork } from 'redux-saga/effects';
import watchFetchDoc from './sagas/watchFetchDoc';
import watchPlaygroundActions from "./sagas/watchPlayground";

export default function* rootSaga(): * {
  console.log('Starting sagas...');
  yield fork(watchFetchDoc);
  yield fork(watchPlaygroundActions);
  console.log('Sagas started');
}
