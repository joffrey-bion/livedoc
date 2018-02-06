// @flow
import type { LoaderState } from '../model/state';
import { newLoaderState } from '../model/state';
import type { Action } from "./actions";
import { DOC_FETCH_ERROR, DOC_FETCHED, FETCH_DOC, RESET } from "./actions/loader";

export default (state: LoaderState = newLoaderState(), action: Action) => {
  switch (action.type) {
    case FETCH_DOC:
      return {
        ...state,
        loading: true,
        loadingError: null,
        url: action.url,
      };
    case DOC_FETCHED:
      // we want to remember the URL from which we loaded the doc
      return {
        ...state,
        loading: false,
      };
    case DOC_FETCH_ERROR:
      // we want to remember the URL from which we loaded the doc
      return {
        ...state,
        loading: false,
        loadingError: action.errorMsg,
      };
    case RESET:
      return newLoaderState();
    default:
      return state;
  }
};
