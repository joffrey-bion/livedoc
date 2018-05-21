// @flow
import type { SagaIterator } from 'redux-saga';
import { put, select, takeLatest } from 'redux-saga/effects'
import { actions } from '../redux/actions/loader';
import { getLoadedDocUrl } from '../redux/doc';

export function* watchRehydrate(): SagaIterator {
  yield takeLatest('persist/REHYDRATE', reloadDocIfNeeded);
}

export function* reloadDocIfNeeded(): SagaIterator {
  const loadedUrl = yield select(state => getLoadedDocUrl(state));
  if (loadedUrl) {
    console.log('loadedUrl', loadedUrl);
    yield put(actions.reloadDoc(loadedUrl))
  }
}
