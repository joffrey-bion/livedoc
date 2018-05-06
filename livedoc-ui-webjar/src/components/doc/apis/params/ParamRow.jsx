// @flow
import * as React from 'react';
import type { ApiParamDoc } from '../../../../model/livedoc';
import { Html } from '../../../shared/content/Html';
import { TypeRefWithFormat } from './TypeRefWithFormat';

export type ParamRowProps = {
  param: ApiParamDoc,
}

export const ParamRow = ({param}: ParamRowProps) => {
  return <tr>
    <td>
      <code>{param.name}</code>
    </td>
    <td>
      <TypeRefWithFormat type={param.type} required={param.required} format={param.format}/>
    </td>
    <td><Html content={param.description}/></td>
  </tr>;
};
