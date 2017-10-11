// @flow
import React from 'react';
import { Button } from 'react-bootstrap';
import { connect } from 'react-redux';
import { actions } from '../../redux/reducer';
import type { State } from '../../model/state';
import './Header.css';

export type HeaderProps = {
  docLoaded: boolean,
  reset: () => void,
}

const Header = ({docLoaded, reset}: HeaderProps) => (<div className="header">
  <h2 className="title">Livedoc</h2>
  {docLoaded && <Button bsStyle="primary" onClick={reset}>Reset</Button>}
</div>);

const mapStateToProps = (state: State) => ({
  docLoaded: !!state.livedoc
});

const mapDispatchToProps = {
  reset: actions.reset,
};

export default connect(mapStateToProps, mapDispatchToProps)(Header);
