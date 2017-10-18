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
});
export type LoaderState = RecordOf<LoaderStateProps>;

export type StateProps = {
  loader: LoaderState,
  livedoc: ?Livedoc
}
export const newState: RecordFactory<StateProps> = Record({
  loader: newLoaderState(),
  livedoc: null,
});
export type State = RecordOf<StateProps>;
