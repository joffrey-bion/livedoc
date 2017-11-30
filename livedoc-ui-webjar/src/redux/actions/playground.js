// @flow
import type { RequestInfo } from "../../model/playground";

export const SEND_HTTP_REQUEST = 'PLAYGROUND/SEND_HTTP_REQUEST';
export const DISPLAY_HTTP_RESPONSE = 'PLAYGROUND/DISPLAY_HTTP_RESPONSE';
export const REQUEST_FAILED = 'PLAYGROUND/REQUEST_FAILED';
export const CANCEL_REQUEST = 'PLAYGROUND/CANCEL_REQUEST';

export type SendHttpRequestAction = { type: SEND_HTTP_REQUEST, request: RequestInfo };
export type DisplayResponseAction = { type: DISPLAY_HTTP_RESPONSE, response: Response };
export type RequestFailedAction = { type: REQUEST_FAILED, error: any };
export type CancelRequestAction = { type: CANCEL_REQUEST };

export type PlaygroundAction = SendHttpRequestAction | DisplayResponseAction | CancelRequestAction;

export const actions = {
  sendRequest: (request: RequestInfo): SendHttpRequestAction => ({ type: SEND_HTTP_REQUEST, request }),
  requestError: (error): RequestFailedAction => ({type: REQUEST_FAILED, error}),
  displayResponse: (response: string): DisplayResponseAction => ({ type: DISPLAY_HTTP_RESPONSE, response }),
  cancelRequest: (): CancelRequestAction => ({type: CANCEL_REQUEST}),
};
