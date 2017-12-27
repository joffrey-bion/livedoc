// @flow
import * as React from 'react';

export type ContentHeaderProps = {
  title: string,
  description?: string,
}

export const ContentHeader = ({title, description}: ContentHeaderProps) => {
  return <div>
    <h1>{title}</h1>
    <hr/>
    {description && <p className="lead">{description}</p>}
  </div>;
};
