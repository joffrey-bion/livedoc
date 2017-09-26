// @flow

import {Record} from 'immutable';
import {State} from '../models/state';
import {Livedoc} from '../models/livedoc';

export const types = {
  FETCH_DOC: 'DOC/FETCH',
  DOC_FETCHED: 'DOC/FETCHED',
  DOC_FETCH_ERROR: 'DOC/FETCH_ERROR',
};

export type Action =
        | { type: 'DOC/FETCH', url: string }
        | { type: 'DOC/FETCHED', jsonDoc: string }
        | { type: 'DOC/FETCH_ERROR', error: Error };

export const actions = {
  fetchDoc: (url: string) => ({ type: types.FETCH_DOC, url }),
  updateDoc: (jsonDoc: any) => ({ type: types.DOC_FETCHED, jsonDoc }),
  fetchError: (error: Error) => ({ type: types.DOC_FETCH_ERROR, error }),
};

export default (state: State = new State(), action: any) => {
  switch (action.type) {
    case types.FETCH_DOC:
      return {
        loading: true,
        url: action.url,
        livedoc: null
      };
    case types.DOC_FETCHED:
      return {
        loading: false,
        url: state.url,
        livedoc: new Livedoc(action.jsonDoc)
      };
    case types.DOC_FETCH_ERROR:
      return {
        loading: false,
        url: state.url,
        livedoc: null
      };
    default:
      return state;
  }
};
