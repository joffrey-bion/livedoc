// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Table } from 'reactstrap';
import type { Livedoc } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getLoadedDoc } from '../../../redux/doc';

export type GeneralInfoProps = {
  livedoc: ?Livedoc
}

const GeneralInfoPresenter = (props: GeneralInfoProps) => {
  if (!props.livedoc) {
    return null;
  }
  return <aside>
    <h4 style={{marginTop: '0.5rem'}}>API INFO</h4>
    <small>
      <Table className="w-50 table-sm">
        <tbody>
        <tr>
          <th scope="row">Base path</th>
          <td>{props.livedoc.apiInfo.baseUrl}</td>
        </tr>
        <tr>
          <th scope="row">Version</th>
          <td>{props.livedoc.apiInfo.version}</td>
        </tr>
        </tbody>
      </Table>
    </small>
  </aside>;
};

const mapStateToProps = (state: State) => ({
  livedoc: getLoadedDoc(state),
});

const mapDispatchToProps = {};

export const GeneralInfo = connect(mapStateToProps, mapDispatchToProps)(GeneralInfoPresenter);
