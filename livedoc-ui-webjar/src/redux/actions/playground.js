// @flow
import type { RequestInfo, ResponseMetaData } from '../../model/playground';

export const SUBMIT_HTTP = 'PLAYGROUND/SUBMIT_HTTP';
export const RECEIVE_METADATA = 'PLAYGROUND/RECEIVE_METADATA';
export const RECEIVE_BODY = 'PLAYGROUND/RECEIVE_BODY';
export const REQUEST_FAILED = 'PLAYGROUND/REQUEST_FAILED';
export const CANCEL_REQUEST = 'PLAYGROUND/CANCEL_REQUEST';

export type SendHttpRequestAction = { type: 'PLAYGROUND/SUBMIT_HTTP', request: RequestInfo };
export type DisplayResponseMetaAction = { type: 'PLAYGROUND/RECEIVE_METADATA', responseMeta: ResponseMetaData };
export type DisplayResponseBodyAction = { type: 'PLAYGROUND/RECEIVE_BODY', responseBody: string };
export type RequestFailedAction = { type: 'PLAYGROUND/REQUEST_FAILED', error: any };
export type CancelRequestAction = { type: 'PLAYGROUND/CANCEL_REQUEST' };

export type PlaygroundAction =
        | SendHttpRequestAction
        | DisplayResponseMetaAction
        | DisplayResponseBodyAction
        | RequestFailedAction
        | CancelRequestAction;

export const actions = {
  sendRequest: (request: RequestInfo): SendHttpRequestAction => ({
    type: SUBMIT_HTTP,
    request,
  }),
  requestError: (error: any): RequestFailedAction => ({
    type: REQUEST_FAILED,
    error,
  }),
  displayResponseMetaData: (responseMeta: ResponseMetaData): DisplayResponseMetaAction => ({
    type: RECEIVE_METADATA,
    responseMeta,
  }),
  displayResponseBody: (body: string): DisplayResponseBodyAction => ({
    type: RECEIVE_BODY,
    responseBody: body,
  }),
  cancelRequest: (): CancelRequestAction => ({type: CANCEL_REQUEST}),
};
