// @flow
import * as React from 'react';
import { Route } from 'react-router-dom';
import TypeTemplate from './content/type/TypeTemplate';

type SidePanelProps = {}

export const SidePanel = (props: SidePanelProps) => <Route path="/types/:typeId" component={TypeTemplate}/>;
