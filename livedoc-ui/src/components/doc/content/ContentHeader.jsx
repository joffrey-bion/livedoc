// @flow
import * as React from 'react';
import { PageHeader } from 'react-bootstrap';

export type ContentHeaderProps = {
  title: string,
  description: ?string,
}

export const ContentHeader = ({title, description}: ContentHeaderProps) => {
  return <div>
    <PageHeader>{title}</PageHeader>
    {description && <blockquote>{description}</blockquote>}
  </div>
};
