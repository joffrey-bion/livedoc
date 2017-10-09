// @flow
import * as React from 'react';
import type { Livedoc } from '../../model/livedoc';

export type GlobalInfoProps = {
  livedoc: Livedoc
}

export const GlobalInfo = (props: GlobalInfoProps) => {
  return <ul>
    <li>Base path: {props.livedoc.basePath}</li>
    <li>Version: {props.livedoc.version}</li>
  </ul>;
};

