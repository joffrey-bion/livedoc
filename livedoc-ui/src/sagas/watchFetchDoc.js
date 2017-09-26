import { apply, call, put, takeLatest } from 'redux-saga/effects'
import type {Action} from '../redux/reducer';
import {actions, types} from '../redux/reducer';

function* fetchDoc(action: Action): * {
  try {
    console.log('Fetching documentation at', action.url);
    const response = yield call(fetch, action.url, {mode: 'cors'});
    const data =  yield apply(response, response.json);
    console.log('Fetched documentation:', data);
    yield put(actions.updateDoc(data));
  } catch (error) {
    console.error('Could not fetch documentation', error);
    yield put(actions.fetchError(error));
  }
}

function* watchFetchDoc() {
  console.log('Watching for FETCH_DOC actions');
  yield takeLatest(types.FETCH_DOC, fetchDoc);
}

export default watchFetchDoc;
