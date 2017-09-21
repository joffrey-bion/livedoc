// @flow
import { Map, Record } from 'immutable';
import {Api} from './api';

export type LivedocShape = {
  apis: Map<String, Api>
}
export type LivedocType = Record<LivedocShape>;

const LivedocRecord: LivedocType = Record({
  apis: Map()
});

export class Livedoc extends LivedocRecord {}
