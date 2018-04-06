// @flow
import { push } from 'react-router-redux';
import { apply, call, put, takeLatest } from 'redux-saga/effects';
import type { FetchDocAction } from '../redux/actions/loader';
import { actions, FETCH_DOC } from '../redux/actions/loader';

function* fetchDoc(action: FetchDocAction): * {
  try {
    console.log('Fetching documentation at', action.url);
    const response: Response = yield call(fetch, action.url, {mode: 'cors', credentials: 'include'});
    if (response.ok) {
      const data = yield apply(response, response.json);
      console.log('Fetched documentation:', data);
      yield put(actions.updateDoc(data));
      yield put(push('/global'));
    } else {
      const errMsg = `Could not fetch documentation, HTTP ${response.status} ${response.statusText}`;
      console.error(errMsg);
      yield put(actions.fetchError(errMsg));
    }
  } catch (error) {
    console.error('Could not fetch documentation', error);
    yield put(actions.fetchError(error.toString()));
  }
}

function* watchFetchDoc(): * {
  console.log('Watching for FETCH_DOC actions');
  yield takeLatest(FETCH_DOC, fetchDoc);
}

export default watchFetchDoc;
