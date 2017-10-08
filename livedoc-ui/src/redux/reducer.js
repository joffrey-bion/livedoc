// @flow
import {newState, newSelectedElement} from '../model/state';
import type {State} from '../model/state';

export const types = {
  FETCH_DOC: 'DOC/FETCH',
  DOC_FETCHED: 'DOC/FETCHED',
  DOC_FETCH_ERROR: 'DOC/FETCH_ERROR',
  SELECT_ELEMENT: 'SELECT_ELEMENT',
};

type ElementType = 'API' | 'TYPE' | 'FLOW';

export type Action =
        | { type: 'DOC/FETCH', url: string }
        | { type: 'DOC/FETCHED', jsonDoc: string }
        | { type: 'DOC/FETCH_ERROR', error: Error }
        | { type: 'SELECT_ELEMENT', id: string, elementType: ElementType };

export const actions = {
  fetchDoc: (url: string) => ({ type: types.FETCH_DOC, url }),
  updateDoc: (jsonDoc: any) => ({ type: types.DOC_FETCHED, jsonDoc }),
  fetchError: (error: Error) => ({ type: types.DOC_FETCH_ERROR, error }),
  selectElement: (id: String, elementType: ElementType) => ({ type: types.SELECT_ELEMENT, id, elementType }),
};

export default (state: State = newState(), action: any) => {
  switch (action.type) {
    case types.FETCH_DOC:
      return state.mergeDeep({
        loader: {
          loading: true,
          url: action.url,
        },
        livedoc: null,
      });
    case types.DOC_FETCHED:
      return state.mergeDeep({
        loader: {
          loading: false,
        },
        livedoc: action.jsonDoc,
      });
    case types.DOC_FETCH_ERROR:
      return state.mergeDeep({
        loader: {
          loading: false,
        },
      });
    case types.SELECT_ELEMENT:
      return state.mergeDeep({
        contentView: {
          selectedElement: newSelectedElement({
            id: action.id,
            type: action.elementType,
          }),
        },
      });
    default:
      return state;
  }
};
