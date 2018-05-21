// @flow
import type { Livedoc } from '../../model/livedoc';

export const FETCH_DOC = 'DOC/FETCH';
export const RELOAD_DOC = 'DOC/RELOAD';
export const DOC_FETCHED = 'DOC/FETCHED';
export const DOC_FETCH_ERROR = 'DOC/FETCH_ERROR';
export const RESET = 'RESET';

export type FetchDocAction = { type: 'DOC/FETCH', url: string };
export type ReloadDocAction = { type: 'DOC/RELOAD', url: string };
export type DocFetchSuccessAction = { type: 'DOC/FETCHED', livedoc: Livedoc, srcUrl: string };
export type DocFetchErrorAction = { type: 'DOC/FETCH_ERROR', errorMsg: string };
export type ResetDocAction = { type: 'RESET' };

export type LoaderAction =
        | FetchDocAction
        | ReloadDocAction
        | DocFetchSuccessAction
        | DocFetchErrorAction
        | ResetDocAction;

export const actions = {
  fetchDoc: (url: string): FetchDocAction => ({ type: FETCH_DOC, url }),
  reloadDoc: (url: string): ReloadDocAction => ({ type: RELOAD_DOC, url }),
  fetchSuccess: (livedoc: Livedoc, srcUrl: string): DocFetchSuccessAction => ({ type: DOC_FETCHED, livedoc, srcUrl }),
  fetchError: (errorMsg: string): DocFetchErrorAction => ({ type: DOC_FETCH_ERROR, errorMsg }),
  reset: (): ResetDocAction => ({type: RESET}),
};
