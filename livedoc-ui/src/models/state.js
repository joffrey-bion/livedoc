// @flow
import {Record} from 'immutable';
import {Livedoc} from './livedoc';

export type StateShape = {
  loading: boolean,
  url: ?string,
  livedoc: ?Livedoc
}
export type StateType = Record<StateShape>;

const StateRecord: StateType = Record({
  loading: false,
  url: null,
  livedoc: null
});

export class State extends StateRecord {}
