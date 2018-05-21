// @flow
import type {
  ApiDoc, ApiGlobalDoc, ApiOperationDoc, ApiTypeDoc, Group, Identified, Livedoc, LivedocID,
} from '../model/livedoc';
import type { DocState, State } from '../model/state';
import type { Action } from './actions';
import { DOC_FETCHED, RESET } from './actions/loader';

export const docReducer = (state: ?DocState = null, action: Action): ?DocState => {
  switch (action.type) {
    case DOC_FETCHED:
      return {
        livedoc: action.livedoc,
        srcUrl: action.srcUrl,
      };
    case RESET:
      return null;
    default:
      return state;
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
  return state.doc && state.doc.livedoc.global;
}

export function getApi(id: LivedocID, state: State): ?ApiDoc {
  return state.doc && getElementById(id, state.doc.livedoc.apis);
}

export function getType(id: LivedocID, state: State): ?ApiTypeDoc {
  return state.doc && getElementById(id, state.doc.livedoc.types);
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
  return state.doc != null;
}

export function getLoadedDoc(state: State): ?Livedoc {
  if (!state.doc || !state.doc.livedoc) {
    throw new Error("No Livedoc loaded");
  }
  return state.doc.livedoc;
}

export function getLoadedDocUrl(state: State): ?string {
  return state.doc && state.doc.srcUrl;
}
