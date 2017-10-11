// @flow
import * as React from 'react';
import type { Livedoc } from '../../model/livedoc';

export type GlobalInfoProps = {
  livedoc: Livedoc
}

export const GlobalInfo = (props: GlobalInfoProps) => {
  return <blockquote>
    <h4>API INFO</h4>
    <small>Base path: {props.livedoc.basePath}</small>
    <small>Version: {props.livedoc.version}</small>
  </blockquote>;
};

