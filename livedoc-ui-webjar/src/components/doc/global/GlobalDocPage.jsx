// @flow
import * as React from 'react';
import './GlobalDocPage.css';

export type GlobalDocPageProps = {
  content: string,
}

export const GlobalDocPage = (props: GlobalDocPageProps) => <div dangerouslySetInnerHTML={{__html: props.content}}/>;
