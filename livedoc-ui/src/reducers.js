import { combineReducers } from 'redux-immutable'
import livedoc from './redux/livedoc';

export default function createReducer() {
  return combineReducers({
    livedoc
  });
}
