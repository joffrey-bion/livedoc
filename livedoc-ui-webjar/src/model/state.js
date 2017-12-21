// @flow
import type { Livedoc } from './livedoc';
import type { ResponseMetaData } from './playground';

export type LoaderState = {
  +loading: boolean,
  +url: ?string
}
export const newLoaderState = () => ({
  loading: false,
  url: null,
});

export type PlaygroundState = {
  +waitingResponse: boolean,
  +streamingResponse: boolean,
  +responseMeta: ?ResponseMetaData,
  +responseBody: ?string,
  +error: any,
}
export function newPlaygroundState(): PlaygroundState {
  return {
    waitingResponse: false,
    streamingResponse: false,
    responseMeta: null,
    responseBody: null,
    error: null,
  };
}

export type State = {
  +loader: LoaderState,
  +livedoc: ?Livedoc,
  +playground: PlaygroundState,
}
export const newState = () => ({
  loader: newLoaderState(),
  livedoc: null,
  playground: newPlaygroundState(),
});
