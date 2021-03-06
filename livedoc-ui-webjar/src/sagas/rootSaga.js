import { fork } from 'redux-saga/effects';
import type { SagaIterator } from 'redux-saga/index';
import { watchFetchDoc } from './watchFetchDoc';
import { watchPlaygroundActions } from './watchPlayground';
import { watchRehydrate } from './watchRehydrate';
import { watchSaveDoc } from './watchSaveDoc';

export function* rootSaga(): SagaIterator {
  yield fork(watchFetchDoc);
  yield fork(watchSaveDoc);
  yield fork(watchPlaygroundActions);
  yield fork(watchRehydrate);
}
