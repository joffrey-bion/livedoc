// @flow
import * as React from 'react';
import { JsonCard } from '../../shared/cards/JsonCard';
import { MonospacedSinkCard } from '../../shared/cards/MonospacedSinkCard';

export type PlaygroundResponseBodyProps = {
  body: ?string,
  contentType: ?string,
}

export const PlaygroundResponseBody = ({body, contentType}: PlaygroundResponseBodyProps) => {
  if (!body) {
    return null;
  }
  if (contentType && contentType.indexOf('application/json') >= 0) {
    return <JsonCard jsonObject={JSON.parse(body)}/>;
  }
  return <MonospacedSinkCard content={body}/>;
};
