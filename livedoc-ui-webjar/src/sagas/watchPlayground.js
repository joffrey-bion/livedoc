// @flow
import { call, put, takeLatest } from 'redux-saga/effects'
import type { RequestInfo } from "../model/playground";
import type { SendHttpRequestAction } from "../redux/actions/playground";
import { actions, SEND_HTTP_REQUEST } from '../redux/actions/playground';

function* sendHttpRequest(action: SendHttpRequestAction): * {
  try {
    const reqInfo: RequestInfo = action.request;
    const options = {
      method: reqInfo.method,
      headers: {
        "Accept": reqInfo.headers.accept,
        "Content-Type": reqInfo.headers.contentType,
      },
      body: reqInfo.body,
      mode: 'cors',
    };

    const response: Response = yield call(fetch, reqInfo.url, options);
    yield put(actions.displayResponse(response));
  } catch (error) {
    console.error('Playground request failed', error);
    yield put(actions.requestError(error));
  }
}

function* watchPlaygroundActions() {
  console.log('Watching for SEND_HTTP_REQUEST actions');
  yield takeLatest(SEND_HTTP_REQUEST, sendHttpRequest);
}

export default watchPlaygroundActions;
