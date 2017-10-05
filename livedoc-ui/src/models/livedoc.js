// @flow
import {List, Map, Record} from 'immutable';
import type { RecordOf, RecordFactory } from 'immutable';
import type {Api} from './api';

type LivedocProps = {
  version: string,
  basePath: string,
  apis: Map<String, List<Api>>
}

export const newLivedoc: RecordFactory<LivedocProps> = Record({
  version: '',
  basePath: '',
  apis: Map()
});

export type Livedoc = RecordOf<LivedocProps>;
