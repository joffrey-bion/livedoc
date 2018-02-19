// @flow
import * as React from 'react';
import type { ApiHeaderDoc, HeaderFilterType } from '../../../../../model/livedoc';
import { CodeElementsList } from '../../../../shared/content/CodeElementsList';

export type HeaderRowProps = {
  header: ApiHeaderDoc,
}

export const HeaderRow = ({header}: HeaderRowProps) => {

  const value = getValueDescription(header.type, header.values);

  return <tr>
    <td>
      <code>{header.name}</code>
    </td>
    <td>{value}</td>
    {header.description && <td>{header.description}</td>}
  </tr>;
};

function getValueDescription(type: HeaderFilterType, values: string[]) {
  switch (type) {
    case 'REQUIRED_MATCHING':
      if (values.length > 1) {
        return ['one of ', <CodeElementsList items={values}/>];
      } else {
        return <CodeElementsList items={values}/>;
      }
    case 'OPTIONAL':
      if (values.length === 1) {
        return ['default: ', <code>{values[0]}</code>];
      } else {
        return '(optional)';
      }
    case 'FORBIDDEN':
      return '(forbidden)';
    case 'DIFFERENT':
      return ['not ', <CodeElementsList items={values}/>];
  }
}
