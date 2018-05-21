// @flow
import type { LoaderState, State } from '../model/state';
import { newLoaderState } from '../model/state';
import type { Action } from './actions';
import { DOC_FETCH_ERROR, DOC_FETCHED, FETCH_DOC, RELOAD_DOC, RESET } from './actions/loader';
import { isDocLoaded } from './doc';

export const loaderReducer = (state: LoaderState = newLoaderState(), action: Action): LoaderState => {
  switch (action.type) {
    case FETCH_DOC:
    case RELOAD_DOC:
      return {
        loading: true,
        loadingError: null,
        url: action.url,
      };
    case DOC_FETCHED:
      return {
        loading: false,
        loadingError: null,
        url: null,
      };
    case DOC_FETCH_ERROR:
      // we want to remember the URL from which we tried to load the doc
      return {
        loading: false,
        loadingError: action.errorMsg,
        url: state.url,
      };
    case RESET:
      return newLoaderState();
    default:
      return state;
  }
};

export type LoadingState = 'IDLE_EMPTY' | 'IDLE_LOADED' | 'LOADING_NEW' | 'RELOADING';

export function getLoadingState(state: State): LoadingState {
  if (state.loader.loading) {
    const loadedUrl = state.doc && state.doc.srcUrl;
    const reloading = state.loader.url === loadedUrl;
    return reloading ? 'RELOADING' : 'LOADING_NEW';
  }
  return isDocLoaded(state) ? 'IDLE_LOADED' : 'IDLE_EMPTY';
}
