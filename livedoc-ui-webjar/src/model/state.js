// @flow
import type { Livedoc } from './livedoc';
import type { ResponseMetaData } from './playground';

export type LoaderState = {
  +loading: boolean,
  +loadingError: ?string,
  +url: ?string
}
export const newLoaderState = () => ({
  loading: false,
  loadingError: null,
  url: computeInitialUrl(),
});

function computeInitialUrl(): string {
  const url = new URL(window.location.href);
  const specifiedUrl = url.searchParams.get('url');
  if (specifiedUrl) {
    return specifiedUrl;
  }
  return window.location.origin + '/jsondoc';
}

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
