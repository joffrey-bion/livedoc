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
  // if using the webjar, then the doc endpoint in on the same server at /jsondoc, so "/jsondoc" is sufficient
  // if using an independent UI, then it is likely that the app server URL ends in /jsondoc, so this string helps anyway
  return '/jsondoc';
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
  +uiVersion: string,
  +loader: LoaderState,
  +livedoc: ?Livedoc,
  +playground: PlaygroundState,
}
export const newState = () => ({
  uiVersion: process.env.REACT_APP_VERSION || '?.?.?',
  loader: newLoaderState(),
  livedoc: null,
  playground: newPlaygroundState(),
});
