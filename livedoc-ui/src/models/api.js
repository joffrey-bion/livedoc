// @flow
import { Record } from 'immutable';
import type { RecordOf, RecordFactory } from 'immutable';

type ApiProps = {
  name: string
}

export const newApi: RecordFactory<ApiProps> = Record({
  name: ''
});

export type Api = RecordOf<ApiProps>;
