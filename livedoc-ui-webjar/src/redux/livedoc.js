// @flow
import type {
  ApiDoc, ApiGlobalDoc, ApiOperationDoc, ApiTypeDoc, Group, Identified, Livedoc, LivedocID,
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

const getElementById = <T : Identified>(id: ?LivedocID, groups: Array<Group<T>>): ?T => {
  if (!id) {
    console.error(new Error("No ID provided to retrieve livedoc element"));
    return null;
  }
  const elements: Array<T> = groups.map(g => g.elements).reduce((acc: T[], elt: T[]) => acc.concat(elt), []);
  return elements.find((e: Identified) => e.livedocId === id);
};

export function getGlobalDoc(state: State): ?ApiGlobalDoc {
  return state.livedoc && state.livedoc.global;
}

export function getApi(id: LivedocID, state: State): ?ApiDoc {
  return state.livedoc && getElementById(id, state.livedoc.apis);
}

export function getType(id: LivedocID, state: State): ?ApiTypeDoc {
  return state.livedoc && getElementById(id, state.livedoc.types);
}

export function getMethod(apiId: LivedocID, methodId: LivedocID, state: State): ?ApiOperationDoc {
  const api: ?ApiDoc = getApi(apiId, state);
  if (!api) {
    console.error("API not found for ID " + apiId);
    return;
  }
  const matchingMethods = api.operations.filter(m => m.livedocId === methodId);
  if (matchingMethods.length === 0) {
    console.error("Method not found for ID " + apiId);
    return;
  }
  return matchingMethods[0];
}

export function isDocLoaded(state: State): boolean {
  return state.livedoc !== null;
}
