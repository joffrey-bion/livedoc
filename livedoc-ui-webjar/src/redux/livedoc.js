// @flow
import type {
  ApiDoc, ApiFlowDoc, ApiGlobalDoc, ApiMethodDoc, ApiObjectDoc, Identified, Livedoc, LivedocID,
} from '../model/livedoc';
import type { State } from '../model/state';
import type { Action } from './actions';
import { DOC_FETCH_ERROR, DOC_FETCHED, FETCH_DOC, RESET } from "./actions/loader";

export default (livedoc: ?Livedoc = null, action: Action) => {
  switch (action.type) {
    case DOC_FETCHED:
      return action.livedoc;
    case FETCH_DOC:
    case DOC_FETCH_ERROR:
    case RESET:
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
    console.error(new Error("No ID provided to retrieve livedoc element"));
    return null;
  }
  const elementsById = dictionarize(elements);
  return elementsById[id];
};

export function getGlobalDoc(state: State): ?ApiGlobalDoc {
  return state.livedoc && state.livedoc.global;
}

export function getApi(id: LivedocID, state: State): ?ApiDoc {
  return state.livedoc && getElementById(id, state.livedoc.apis);
}

export function getType(id: LivedocID, state: State): ?ApiObjectDoc {
  return state.livedoc && getElementById(id, state.livedoc.objects);
}

export function getFlow(id: LivedocID, state: State): ?ApiFlowDoc {
  return state.livedoc && getElementById(id, state.livedoc.flows);
}

export function getMethod(apiId: LivedocID, methodId: LivedocID, state: State): ?ApiMethodDoc {
  const api: ?ApiDoc = getApi(apiId, state);
  if (!api) {
    console.error("API not found for ID " + apiId);
    return;
  }
  const matchingMethods = api.methods.filter(m => m.livedocId === methodId);
  if (matchingMethods.length === 0) {
    console.error("Method not found for ID " + apiId);
    return;
  }
  return matchingMethods[0];
}

export function isDocLoaded(state: State): boolean {
  return state.livedoc !== null;
}
