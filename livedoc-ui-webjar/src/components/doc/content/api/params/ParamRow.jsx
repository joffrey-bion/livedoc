// @flow
import * as React from 'react';
import type { ApiParamDoc } from '../../../../../model/livedoc';
import { TypeInfoWithFormat } from './TypeInfoWithFormat';

export type ParamRowProps = {
  param: ApiParamDoc,
}

export const ParamRow = ({param}: ParamRowProps) => {
  return <tr>
    <td>{param.name}</td>
    <td>
      <TypeInfoWithFormat type={param.type}
                          required={param.required === 'undefined' ? null : param.required === 'true'}
                          format={param.format}/>
    </td>
    <td>{param.description}</td>
  </tr>;
};
