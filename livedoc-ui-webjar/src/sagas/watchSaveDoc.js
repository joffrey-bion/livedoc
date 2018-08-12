// @flow
import { saveAs } from 'file-saver';
import type { SagaIterator } from 'redux-saga';
import { call, select, takeLatest } from 'redux-saga/effects';
import type { Livedoc } from '../model/livedoc';
import { SAVE_DOC } from '../redux/actions/loader';
import { getLoadedDoc } from '../redux/doc';

export function* watchSaveDoc(): SagaIterator {
  yield takeLatest(SAVE_DOC, saveDoc);
}

function* saveDoc(): SagaIterator {
  try {
    const livedoc: Livedoc = yield select(state => getLoadedDoc(state));

    const filename = getFilename(livedoc);
    const data = toJsonBlob(livedoc);

    yield call(saveAs, data, filename);
  } catch (error) {
    console.error("Could not save doc to file", error);
  }
}

function getFilename(livedoc: Livedoc): string {
  const {name , version} = livedoc.apiInfo;
  const namePart = (name && `${name}-`) || '';
  const versionPart = (version && `-${version}`) || '';
  return `${namePart}livedoc${versionPart}.json`;
}

function toJsonBlob(livedoc: Livedoc): Blob {
  const json = JSON.stringify(livedoc);
  return new Blob([json], {type: "application/json;charset=utf-8"});
}
