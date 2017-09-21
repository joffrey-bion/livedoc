import { call, put, takeLatest } from 'redux-saga/effects'
import type {Action} from '../redux/livedoc';

function* fetchDoc(action: Action): * {
  try {
    console.log('Fetching documentation at', action.url);
    const data = yield call(fetch, action.url);
    console.log('Fetched documentation:', data);
    yield put(actions.updateDoc(data));
  } catch (error) {
    console.error('Could not fetch documentation', error);
    yield put({type: "FETCH_FAILED", error});
  }
}

function* watchFetchDoc() {
  console.log('Watching for FETCH_DOC actions');
  yield takeLatest(types.FETCH_DOC, fetchDoc);
}

export default watchFetchDoc;
