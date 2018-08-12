// @flow
import { APP_VERSION } from '../App';
import type { Livedoc } from './livedoc';
import type { ResponseMetaData } from './playground';

export type LoaderState = {
  +loading: boolean,
  +loadingError: ?string,
  +url: ?string,
}
export const newLoaderState = (): LoaderState => ({
  loading: false,
  loadingError: null,
  url: null,
});

export type DocState = {
  +livedoc: Livedoc,
  +srcUrl: ?string,
}

export type PlaygroundState = {
  +waitingResponse: boolean,
  +streamingResponse: boolean,
  +responseMeta: ?ResponseMetaData,
  +responseBody: ?string,
  +error: any,
}
export const newPlaygroundState = (): PlaygroundState => ({
  waitingResponse: false,
  streamingResponse: false,
  responseMeta: null,
  responseBody: null,
  error: null,
});

export type State = {
  +uiVersion: string,
  +loader: LoaderState,
  +doc: ?DocState,
  +playground: PlaygroundState,
}
export const newState = () => ({
  uiVersion: APP_VERSION,
  loader: newLoaderState(),
  doc: null,
  playground: newPlaygroundState(),
});
