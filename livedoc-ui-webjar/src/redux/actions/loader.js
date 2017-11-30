// @flow
import type { Livedoc } from "../../model/livedoc";

export const FETCH_DOC = 'DOC/FETCH';
export const DOC_FETCHED = 'DOC/FETCHED';
export const DOC_FETCH_ERROR = 'DOC/FETCH_ERROR';
export const RESET = 'RESET';

export type FetchDocAction = { type: FETCH_DOC, url: string };
export type UpdateDocAction = { type: DOC_FETCHED, livedoc: Livedoc };
export type DocFetchErrorAction = { type: DOC_FETCH_ERROR };
export type ResetDocAction = { type: RESET };

export type LoaderAction = FetchDocAction | UpdateDocAction | DocFetchErrorAction | ResetDocAction;

export const actions = {
  fetchDoc: (url: string): FetchDocAction => ({ type: FETCH_DOC, url }),
  updateDoc: (livedoc: Livedoc): UpdateDocAction => ({ type: DOC_FETCHED, livedoc }),
  fetchError: (error: Error): DocFetchErrorAction => ({ type: DOC_FETCH_ERROR, error }),
  reset: (): ResetDocAction => ({type: RESET}),
};
