// @flow
import type { Livedoc } from '../../model/livedoc';

export const FETCH_DOC = 'DOC/FETCH';
export const LOAD_DOC_FROM_FILE = 'DOC/LOAD_FROM_FILE';
export const RELOAD_DOC = 'DOC/RELOAD';
export const DOC_FETCHED = 'DOC/FETCHED';
export const DOC_LOADED_FROM_FILE = 'DOC/LOADED_FROM_FILE';
export const DOC_FETCH_ERROR = 'DOC/FETCH_ERROR';
export const SAVE_DOC = 'SAVE_DOC';
export const RESET = 'RESET';

export type FetchDocAction = { type: 'DOC/FETCH', url: string };
export type LoadDocFromFileAction = { type: 'DOC/LOAD_FROM_FILE', file: File };
export type ReloadDocAction = { type: 'DOC/RELOAD', url: string };
export type DocFetchSuccessAction = { type: 'DOC/FETCHED', livedoc: Livedoc, srcUrl: string };
export type DocLoadFileSuccessAction = { type: 'DOC/LOADED_FROM_FILE', livedoc: Livedoc, file: File };
export type DocFetchErrorAction = { type: 'DOC/FETCH_ERROR', errorMsg: string };
export type SaveDocAction = { type: 'SAVE_DOC' };
export type ResetDocAction = { type: 'RESET' };

export type LoaderAction =
        | FetchDocAction
        | LoadDocFromFileAction
        | ReloadDocAction
        | DocFetchSuccessAction
        | DocLoadFileSuccessAction
        | DocFetchErrorAction
        | SaveDocAction
        | ResetDocAction;

export const actions = {
  fetchDoc: (url: string): FetchDocAction => ({ type: FETCH_DOC, url }),
  loadDocFromFile: (file: File): LoadDocFromFileAction => ({ type: LOAD_DOC_FROM_FILE, file }),
  reloadDoc: (url: string): ReloadDocAction => ({ type: RELOAD_DOC, url }),
  fetchSuccess: (livedoc: Livedoc, srcUrl: string): DocFetchSuccessAction => ({ type: DOC_FETCHED, livedoc, srcUrl }),
  loadFromFileSuccess: (livedoc: Livedoc, file: File): DocLoadFileSuccessAction => ({ type: DOC_LOADED_FROM_FILE, livedoc, file }),
  fetchError: (errorMsg: string): DocFetchErrorAction => ({ type: DOC_FETCH_ERROR, errorMsg }),
  saveDoc: (): SaveDocAction => ({type: SAVE_DOC}),
  reset: (): ResetDocAction => ({type: RESET}),
};
