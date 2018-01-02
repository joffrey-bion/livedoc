// @flow
import * as React from 'react';
import type { ApiParamDoc } from '../../../../../model/livedoc';
import { TypeRefWithFormat } from './TypeRefWithFormat';

export type ParamRowProps = {
  param: ApiParamDoc,
}

export const ParamRow = ({param}: ParamRowProps) => {
  return <tr>
    <td>{param.name}</td>
    <td>
      <TypeRefWithFormat type={param.type} required={param.required} format={param.format}/>
    </td>
    <td>{param.description}</td>
  </tr>;
};
