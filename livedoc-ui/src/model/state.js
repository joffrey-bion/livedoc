// @flow
import type { RecordFactory, RecordOf } from 'immutable';
import { Record } from 'immutable';
import type { Livedoc } from './livedoc';

type LoaderStateProps = {
  loading: boolean,
  url: ?string
}
export const newLoaderState: RecordFactory<LoaderStateProps> = Record({
  loading: false,
  url: null,
}, 'LoaderState');
export type LoaderState = RecordOf<LoaderStateProps>;

export type State = {
  loader: LoaderState,
  livedoc: ?Livedoc
}
export const newState = () => ({
  loader: newLoaderState(),
  livedoc: null,
});
