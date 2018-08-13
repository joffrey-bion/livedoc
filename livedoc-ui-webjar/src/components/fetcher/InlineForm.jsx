// @flow
import * as React from 'react';
import { Input, InputGroup, InputGroupButton } from 'reactstrap';

type InlineFormProps = {
  initialValue: string,
  hintText: string,
  onSubmit: string => void,
  btnLabel: string,
  btnTitle: ?string,
  [otherProps: any]: any,
}

type InlineFormState = {
  value: string
}

export class InlineForm extends React.Component<InlineFormProps, InlineFormState> {

  constructor(props: InlineFormProps) {
    super(props);
    this.state = {
      value: props.initialValue,
    };
  }

  setValue(value: string) {
    this.setState({value});
  }

  render() {
    return <InputGroup {...this.props.otherProps}>
      <Input type='text'
             value={this.state.value}
             placeholder={this.props.hintText}
             onChange={e => this.setValue(e.target.value)}/>
      <InputGroupButton color="info"
                        title={this.props.btnTitle}
                        onClick={() => this.props.onSubmit(this.state.value)}>{this.props.btnLabel}</InputGroupButton>
    </InputGroup>;
  }
}
