// @flow
import type { Livedoc } from '../model/livedoc';
import type { LoaderState } from '../model/state';
import { newLoaderState } from '../model/state';

export const types = {
  FETCH_DOC: 'DOC/FETCH',
  DOC_FETCHED: 'DOC/FETCHED',
  DOC_FETCH_ERROR: 'DOC/FETCH_ERROR',
  RESET: 'RESET',
};

export type Action =
        | { type: 'DOC/FETCH', url: string }
        | { type: 'DOC/FETCHED', livedoc: Livedoc }
        | { type: 'DOC/FETCH_ERROR', error: Error }
        | { type: 'RESET' };

export const actions = {
  fetchDoc: (url: string) => ({ type: types.FETCH_DOC, url }),
  updateDoc: (livedoc: Livedoc) => ({ type: types.DOC_FETCHED, livedoc }),
  fetchError: (error: Error) => ({ type: types.DOC_FETCH_ERROR, error }),
  reset: () => ({ type: types.RESET }),
};

export default (state: LoaderState = newLoaderState(), action: Action) => {
  switch (action.type) {
    case types.FETCH_DOC:
      return state.merge({
          loading: true,
          url: action.url,
        });
    case types.DOC_FETCHED:
      return state.merge({
          loading: false,
        });
    case types.DOC_FETCH_ERROR:
      return state.merge({
          loading: false,
      });
    case types.RESET:
      return newLoaderState();
    default:
      return state;
  }
};
