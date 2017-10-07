// @flow
import * as React from 'react';
import type { Livedoc } from '../../model/livedoc';

type Props = {
  livedoc: Livedoc
}

export const GlobalInfo = (props: Props) => {
  return <ul>
    <li>Base path: {props.livedoc.basePath}</li>
    <li>Version: {props.livedoc.version}</li>
  </ul>;
};

