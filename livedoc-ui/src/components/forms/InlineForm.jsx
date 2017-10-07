// @flow
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import * as React from 'react';

type Props = {
  initialValue: string,
  hintText: string,
  onSubmit: string => void,
  btnLabel: string
}

type State = {
  value: string
}

export class InlineForm extends React.Component<Props, State> {

  constructor(props: Props) {
    super(props);
    this.state = {
      value: props.initialValue,
    };
  }

  setValue(value: string) {
    this.setState({value});
  };

  render() {
    return <div>
      <TextField type='text' value={this.state.value} hintText={this.props.hintText}
                 onChange={(e, val) => this.setValue(val)}/>
      <RaisedButton label={this.props.btnLabel} primary onClick={() => this.props.onSubmit(this.state.value)}/>
    </div>;
  }
}
