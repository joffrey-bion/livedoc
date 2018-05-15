// @flow
import * as React from 'react';
import './GlobalDocPage.css';
import { Html } from '../../shared/content/Html';

export type GlobalDocPageProps = {
  content: string,
}

export const GlobalDocPage = (props: GlobalDocPageProps) => <Html content={props.content}/>;
