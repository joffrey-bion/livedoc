// @flow
import * as React from 'react';
import './GlobalHome.css';

export type GlobalHomeProps = {
  content: string,
}

export const GlobalHome = (props: GlobalHomeProps) => <div dangerouslySetInnerHTML={{__html: props.content}}/>;
