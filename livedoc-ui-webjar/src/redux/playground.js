// @flow
import type { PlaygroundState } from '../model/state';
import { newPlaygroundState } from '../model/state';
import type { Action } from './actions';
import { RESET } from './actions/loader';
import { CANCEL_REQUEST, RECEIVE_BODY, RECEIVE_METADATA, REQUEST_FAILED, SUBMIT_HTTP } from './actions/playground';

export default (state: PlaygroundState = newPlaygroundState(), action: Action): PlaygroundState => {
  switch (action.type) {
    case SUBMIT_HTTP:
      return {
        waitingResponse: true,
        streamingResponse: false,
        responseMeta: null,
        responseBody: null,
        error: null,
      };
    case RECEIVE_METADATA:
      return {
        ...state,
        waitingResponse: false,
        streamingResponse: true,
        responseMeta: action.responseMeta,
      };
    case RECEIVE_BODY:
      return {
        ...state,
        streamingResponse: false,
        responseBody: action.responseBody,
      };
    case CANCEL_REQUEST:
      return {
        waitingResponse: false,
        streamingResponse: false,
        responseMeta: null,
        responseBody: null,
        error: null,
      };
    case REQUEST_FAILED:
      return {
        waitingResponse: false,
        streamingResponse: false,
        responseMeta: null,
        responseBody: null,
        error: action.error,
      };
    case RESET:
      return newPlaygroundState();
    default:
      return state;
  }
};
