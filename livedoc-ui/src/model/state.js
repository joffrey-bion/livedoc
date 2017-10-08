// @flow
import type {RecordFactory, RecordOf} from 'immutable';
import {Record} from 'immutable';
import type {Livedoc} from './livedoc';

export type ElementType = 'API' | 'TYPE' | 'FLOW';

type LoaderStateProps = {
  loading: boolean,
  url: ?string
}
export const newLoaderState: RecordFactory<LoaderStateProps> = Record({
  loading: false,
  url: null,
});
export type LoaderState = RecordOf<LoaderStateProps>;


export type SelectedElementProps = {
  id: string,
  type: ElementType,
}
export const newSelectedElement: RecordFactory<SelectedElementProps> = Record({
  id: '',
  type: 'API',
});
export type SelectedElement = RecordOf<SelectedElementProps>;


export type ContentViewProps = {
  selectedElement: ?SelectedElement,
}
const newContentView: RecordFactory<ContentViewProps> = Record({
  selectedElement: null,
});
export type ContentView = RecordOf<ContentViewProps>;


export type StateProps = {
  loader: LoaderState,
  contentView: ContentView,
  livedoc: ?Livedoc
}
export const newState: RecordFactory<StateProps> = Record({
  loader: newLoaderState(),
  contentView: newContentView(),
  livedoc: null,
});
export type State = RecordOf<StateProps>;
