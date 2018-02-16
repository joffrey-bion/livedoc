// @flow
import * as React from 'react';
import { HorizontalList } from './HorizontalList';

export type CodeElementsListProps = {
  items: string[],
  delimiter?: string,
}

export const CodeElementsList = ({items, delimiter}: CodeElementsListProps) => {
  const codeElems = items.map((s, index) => <code key={index}>{s}</code>);
  return <HorizontalList items={codeElems} delimiter={delimiter}/>
}
