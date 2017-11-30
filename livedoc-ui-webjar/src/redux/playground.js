// @flow
import type { PlaygroundState } from "../model/state";
import { newPlaygroundState } from '../model/state';
import type { Action } from "./actions";
import { CANCEL_REQUEST, DISPLAY_HTTP_RESPONSE, SEND_HTTP_REQUEST } from "./actions/playground";

export default (state: PlaygroundState = newPlaygroundState(), action: Action): PlaygroundState => {
  switch (action.type) {
    case SEND_HTTP_REQUEST:
      return {
        loading: true,
        response: null,
      };
    case DISPLAY_HTTP_RESPONSE:
      return {
        loading: false,
        response: action.response,
      };
    case CANCEL_REQUEST:
      return {
        loading: false,
        response: null,
      };
    default:
      return state;
  }
};
