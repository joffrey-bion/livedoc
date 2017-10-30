// @flow
import * as React from 'react';
import { Input, InputGroup, InputGroupButton } from 'reactstrap';

type Props = {
  initialValue: string,
  hintText: string,
  onSubmit: string => void,
  btnLabel: string,
  [otherProps: any]: any,
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
    return <InputGroup {...this.props.otherProps}>
      <Input type='text'
             value={this.state.value}
             placeholder={this.props.hintText}
             onChange={(e, val) => this.setValue(val)}/>
      <InputGroupButton color="info"
                        onClick={() => this.props.onSubmit(this.state.value)}>{this.props.btnLabel}</InputGroupButton>
    </InputGroup>;
  }
}
