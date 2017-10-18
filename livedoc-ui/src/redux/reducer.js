// @flow
import type { ApiDoc, ApiFlowDoc, ApiObjectDoc, Identified, Livedoc, LivedocID } from '../model/livedoc';
import type { State } from '../model/state';
import { newState } from '../model/state';

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
    case types.RESET:
      return newState();
    default:
      return state;
  }
};

const dictionarize = <T>(elementsByWhatever: {[string]: $ReadOnlyArray<T & Identified>}) => {
  const elementsById: {[id: LivedocID]: T} = {};
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

const getElementById = <T>(id: ?LivedocID, elements: {[string]: $ReadOnlyArray<T & Identified>}): ?T => {
  if (!id) {
    return null;
  }
  const elementsById = dictionarize(elements);
  return elementsById[id];
};

export const getApi = (id: LivedocID, state: State): ?ApiDoc => getElementById(id, state.livedoc.apis);
export const getType = (id: LivedocID, state: State): ?ApiObjectDoc => getElementById(id, state.livedoc.objects);
export const getFlow = (id: LivedocID, state: State): ?ApiFlowDoc => getElementById(id, state.livedoc.flows);
