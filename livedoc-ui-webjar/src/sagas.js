import { fork } from 'redux-saga/effects';
import watchFetchDoc from './sagas/watchFetchDoc';

export default function* rootSaga(): * {
  console.log('Starting rootSaga');
  yield fork(watchFetchDoc);
  console.log('RootSaga started');
}
