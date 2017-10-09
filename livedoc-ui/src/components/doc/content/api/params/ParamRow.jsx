// @flow
import * as React from 'react';
import type { ApiParamDoc } from '../../../../../model/livedoc';
import { TypeInfoWithFormat } from './TypeInfoWithFormat';

export type ParamRowProps = {
  param: ApiParamDoc,
  onTypeClick: (id: string) => void,
}

export const ParamRow = ({param, onTypeClick}: ParamRowProps) => {
  return <tr>
    <td>{param.name}</td>
    <td><TypeInfoWithFormat type={param.type}
                            required={param.required}
                            format={param.format}
                            onTypeClick={onTypeClick}/></td>
    <td>{param.description}</td>
  </tr>;
};
