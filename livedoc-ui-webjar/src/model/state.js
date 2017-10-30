// @flow
import type { Livedoc } from './livedoc';

type LoaderState = {
  loading: boolean,
  url: ?string
}
export const newLoaderState = () => ({
  loading: false,
  url: null,
});

export type State = {
  loader: LoaderState,
  livedoc: ?Livedoc
}
export const newState = () => ({
  loader: newLoaderState(),
  livedoc: null,
});
