// @flow
import type { ApiDoc, ApiFlowDoc, ApiObjectDoc, Identified, Livedoc } from '../model/livedoc';
import type { State } from '../model/state';
import { newState } from '../model/state';

export const types = {
  FETCH_DOC: 'DOC/FETCH',
  DOC_FETCHED: 'DOC/FETCHED',
  DOC_FETCH_ERROR: 'DOC/FETCH_ERROR',
  SELECT_ELEMENT: 'SELECT_ELEMENT',
  RESET: 'RESET',
};

export type Action =
        | { type: 'DOC/FETCH', url: string }
        | { type: 'DOC/FETCHED', livedoc: Livedoc }
        | { type: 'DOC/FETCH_ERROR', error: Error }
        | { type: 'SELECT_ELEMENT', id: string }
        | { type: 'RESET' };

export const actions = {
  fetchDoc: (url: string) => ({ type: types.FETCH_DOC, url }),
  updateDoc: (livedoc: Livedoc) => ({ type: types.DOC_FETCHED, livedoc }),
  fetchError: (error: Error) => ({ type: types.DOC_FETCH_ERROR, error }),
  selectElement: (id: String) => ({ type: types.SELECT_ELEMENT, id }),
  reset: () => ({ type: types.RESET }),
};

export default (state: State = newState(), action: Action) => {
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
        livedoc: action.livedoc,
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
          selectedElementId: action.id,
        },
      });
    case types.RESET:
      return newState();
    default:
      return state;
  }
};

const dictionarize = <T>(elementsByWhatever: {[string]: $ReadOnlyArray<T & Identified>}) => {
  const elementsById: {[id: string]: T} = {};
  for (let key in elementsByWhatever) {
    if (elementsByWhatever.hasOwnProperty(key)) {
      const elems: $ReadOnlyArray<T & Identified> = elementsByWhatever[key];
      elems.forEach(elem => {
        elementsById[elem.livedocId] = elem;
      });
    }
  }
  return elementsById;
};

const getElementById = <T>(state: State, getElements: (s: State) => {[string]: $ReadOnlyArray<T & Identified>}): ?T => {
  const id = state.contentView.selectedElementId;
  if (!id) {
    return null;
  }
  const elementsById = dictionarize(getElements(state));
  return elementsById[id];
};

export const getSelectedApi = (state: State): ?ApiDoc => getElementById(state, s => s.livedoc.apis);
export const getSelectedType = (state: State): ?ApiObjectDoc => getElementById(state, s => s.livedoc.objects);
export const getSelectedFlow = (state: State): ?ApiFlowDoc => getElementById(state, s => s.livedoc.flows);
