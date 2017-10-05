// @flow
import type {RecordFactory, RecordOf} from 'immutable';
import {Record} from 'immutable';
import type {Livedoc} from './livedoc';

export type StateProps = {
  loading: boolean, url: ?string, livedoc: ?Livedoc
}

export const newState: RecordFactory<StateProps> = Record({
  loading: false,
  url: null,
  livedoc: null,
});

export type State = RecordOf<StateProps>;
