// @flow
import type { Livedoc } from './livedoc';

export type LoaderState = {
  +loading: boolean,
  +url: ?string
}
export const newLoaderState = () => ({
  loading: false,
  url: null,
});

export type PlaygroundState = {
  +loading: boolean,
  +response: ?Response,
}
export const newPlaygroundState = () => ({
  loading: false,
  response: null,
});

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
