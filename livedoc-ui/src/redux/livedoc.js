// @flow

import { fromJS } from 'immutable';
import {Livedoc} from '../models/livedoc';

export const types = {
  FETCH_DOC: 'DOC/FETCH',
  DOC_FETCHED: 'DOC/FETCHED',
};

export type Action =
        | { type: types.FETCH_DOC, url: string }
        | { type: types.DOC_FETCHED, jsonDoc: string };

export const actions = {
  fetchDoc: (url: string) => ({ type: types.FETCH_DOC, url }),
  updateDoc: (jsonDoc: string) => ({ type: types.DOC_FETCHED, jsonDoc }),
};

export default (state: ?Livedoc = null, action: Action) => {
  switch (action.type) {
    case types.FETCH_DOC:
      return null; // remove current doc if fetching another one
    case types.DOC_FETCHED:
      return fromJS(JSON.parse(action.jsonDoc));
    default:
      return state;
  }
};
