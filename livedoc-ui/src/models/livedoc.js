// @flow
import { Map, Record } from 'immutable';
import {Api} from './api';

export type LivedocShape = {
  version: string,
  basePath: string,
  apis: Map<String, Api>
}
export type LivedocType = Record<LivedocShape>;

const LivedocRecord: LivedocType = Record({
  version: '',
  basePath: '',
  apis: Map()
});

export class Livedoc extends LivedocRecord {}
