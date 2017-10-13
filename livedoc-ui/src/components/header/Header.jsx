// @flow
import React from 'react';
import { Button } from 'reactstrap';
import { connect } from 'react-redux';
import { actions } from '../../redux/reducer';
import type { State } from '../../model/state';
import './Header.css';
import logo from './livedoc-logo-round-white.svg';

export type HeaderProps = {
  docLoaded: boolean,
  reset: () => void,
}

const Header = ({docLoaded, reset}: HeaderProps) => (<div className="header">
  <h2 className="title"><img src={logo} alt="logo" width={30}/> Livedoc</h2>
  {docLoaded && <Button color="info" onClick={reset}>Reset</Button>}
</div>);

const mapStateToProps = (state: State) => ({
  docLoaded: !!state.livedoc
});

const mapDispatchToProps = {
  reset: actions.reset,
};

export default connect(mapStateToProps, mapDispatchToProps)(Header);
