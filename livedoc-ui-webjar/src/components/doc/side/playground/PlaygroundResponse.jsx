// @flow
import * as React from 'react';

export type PlaygroundResponseProps = {
  response: Response,
}

export const PlaygroundResponse = ({response}: PlaygroundResponseProps) => {
  if (!response) {
    return null;
  }
  return <div>
    Status: {response.status}
  </div>
};
