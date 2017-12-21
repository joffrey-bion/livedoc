// @flow
import { push } from 'react-router-redux';
import { apply, call, put, takeLatest } from 'redux-saga/effects';
import type { FetchDocAction } from '../redux/actions/loader';
import { actions, FETCH_DOC } from '../redux/actions/loader';

function* fetchDoc(action: FetchDocAction): * {
  try {
    console.log('Fetching documentation at', action.url);
    const response = yield call(fetch, action.url, {mode: 'cors'});
    const data = yield apply(response, response.json);
    console.log('Fetched documentation:', data);
    yield put(actions.updateDoc(data));
    yield put(push('/global'));
  } catch (error) {
    console.error('Could not fetch documentation', error);
    yield put(actions.fetchError(error));
  }
}

function* watchFetchDoc(): * {
  console.log('Watching for FETCH_DOC actions');
  yield takeLatest(FETCH_DOC, fetchDoc);
}

export default watchFetchDoc;
