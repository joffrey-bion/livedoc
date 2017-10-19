// @flow
import type { ApiDoc, ApiFlowDoc, ApiObjectDoc, Identified, Livedoc, LivedocID } from '../model/livedoc';
import type { State } from '../model/state';
import type { Action } from './loader';
import { types } from './loader';

export default (livedoc: ?Livedoc = null, action: Action) => {
  switch (action.type) {
    case types.DOC_FETCHED:
      return action.livedoc;
    case types.FETCH_DOC:
    case types.DOC_FETCH_ERROR:
    case types.RESET:
      return null;
    default:
      return livedoc;
  }
};

type ElementArray<T> = $ReadOnlyArray<T & Identified>;
type ElementsById<T> = { [id: LivedocID]: T };

const dictionarize = <T>(elementsByWhatever: { [string]: ElementArray<T> }): ElementsById<T> => {
  const arraysOfElements: Array<ElementArray<T>> = Object.keys(elementsByWhatever).map(k => elementsByWhatever[k]);
  return arraysOfElements.reduce((elemsById: ElementsById<T>, elems: ElementArray<T>) => {
    elems.forEach(e => {
      elemsById[e.livedocId] = e;
    });
    return elemsById;
  }, {});
};

const getElementById = <T>(id: ?LivedocID, elements: { [string]: ElementArray<T> }): ?T => {
  if (!id) {
    return null;
  }
  const elementsById = dictionarize(elements);
  return elementsById[id];
};

export const getApi = (id: LivedocID, state: State): ?ApiDoc => getElementById(id, state.livedoc.apis);
export const getType = (id: LivedocID, state: State): ?ApiObjectDoc => getElementById(id, state.livedoc.objects);
export const getFlow = (id: LivedocID, state: State): ?ApiFlowDoc => getElementById(id, state.livedoc.flows);

export const isDocLoaded = (state: State) => state.livedoc !== null;
