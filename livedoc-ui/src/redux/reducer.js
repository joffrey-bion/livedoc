// @flow

import {fromJS} from 'immutable';
import {State} from '../models/state';

export const types = {
  FETCH_DOC: 'DOC/FETCH',
  DOC_FETCHED: 'DOC/FETCHED',
};

export type Action =
        | { type: 'DOC/FETCH', url: string }
        | { type: 'DOC/FETCHED', jsonDoc: string };

export const actions = {
  fetchDoc: (url: string) => ({ type: types.FETCH_DOC, url }),
  updateDoc: (jsonDoc: string) => ({ type: types.DOC_FETCHED, jsonDoc }),
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
        livedoc: fromJS(JSON.parse(action.jsonDoc))
      };
    default:
      return state;
  }
};
