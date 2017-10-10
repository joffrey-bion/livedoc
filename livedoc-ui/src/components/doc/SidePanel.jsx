// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { ApiDoc, ApiFlowDoc, ApiObjectDoc } from '../../model/livedoc';
import type { State } from '../../model/state';
import { getSelectedApi, getSelectedType, getSelectedFlow } from '../../redux/reducer';
import { TypeTemplate } from './content/type/TypeTemplate';

type SidePanelProps = {
  selectedApi: ApiDoc,
  selectedType: ApiObjectDoc,
  selectedFlow: ApiFlowDoc,
}

const SidePanel = ({selectedApi, selectedType, selectedFlow}: SidePanelProps) => {
  if (selectedApi) {
    return null;
  }
  if (selectedType) {
    return <TypeTemplate typeDoc={selectedType}/>;
  }
  if (selectedFlow) {
    return null;
  }
  return null;
};

const mapStateToProps = (state: State) => ({
  selectedApi: getSelectedApi(state),
  selectedType: getSelectedType(state),
  selectedFlow: getSelectedFlow(state),
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(SidePanel);
