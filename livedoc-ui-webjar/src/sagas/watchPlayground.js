// @flow
import { apply, call, put, takeLatest } from 'redux-saga/effects'
import type { RequestInfo, ResponseMetaData } from '../model/playground';
import type { SendHttpRequestAction } from '../redux/actions/playground';
import { actions, SUBMIT_HTTP } from '../redux/actions/playground';

function* watchPlaygroundActions(): * {
  console.log('Watching for SUBMIT_HTTP actions');
  yield takeLatest(SUBMIT_HTTP, sendHttpRequest);
}

function* sendHttpRequest(action: SendHttpRequestAction): * {
  try {
    const reqInfo: RequestInfo = action.request;
    const options = toFetchOptions(reqInfo);

    const response: Response = yield call(fetch, reqInfo.url, options);
    const responseMeta: ResponseMetaData = extractMetaData(response);
    yield put(actions.displayResponseMetaData(responseMeta));

    const responseBody: string = yield apply(response, response.text);
    yield put(actions.displayResponseBody(responseBody));
  } catch (error) {
    console.error('Playground request failed', error);
    yield put(actions.requestError(error));
  }
}

function toFetchOptions(requestInfo: RequestInfo): ResponseMetaData {
  return {
    method: requestInfo.method,
    headers: {
      'Accept': requestInfo.headers.accept,
      'Content-Type': requestInfo.headers.contentType,
    },
    body: requestInfo.body,
    mode: 'cors',
  };
}

function extractMetaData(response: Response): ResponseMetaData {
  return {
    headers: response.headers,
    status: response.status,
    statusText: response.statusText,
  };
}

export default watchPlaygroundActions;
