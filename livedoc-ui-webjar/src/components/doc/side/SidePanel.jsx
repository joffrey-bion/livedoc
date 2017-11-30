// @flow
import * as React from 'react';
import { Route, Switch } from 'react-router-dom';
import TypeTemplate from './TypeExample';
import Playground from './playground/Playground';

type SidePanelProps = {}

export const SidePanel = (props: SidePanelProps) => (<Switch>
  <Route path="/types/:typeId" component={TypeTemplate}/>
  <Route path="/apis/:apiId/:methodId" component={Playground}/>
</Switch>);
