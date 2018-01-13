// @flow
import * as React from 'react';
import {Button, Col, Form, FormGroup, Input, Label} from 'reactstrap';
import type {ApiBodyObjectDoc, ApiMethodDoc, ApiVerb} from '../../model/livedoc';
import type {RequestInfo} from '../../model/playground';

export type PlaygroundFormProps = {
  basePath: string,
  methodDoc: ApiMethodDoc,
  onSubmit: RequestInfo => void
}

type PlaygroundFormState = {
  url: string,
  method: ApiVerb,
  acceptHeader: string,
  contentType: string,
  body: string,
}

const PlaygroundFormRow = ({label, id, children}) => (<FormGroup row className="align-items-center">
  <Col>
    <Label for={id}>{label}</Label>
  </Col>
  <Col sm={8}>
    {children}
  </Col>
</FormGroup>);

const Select = ({id, name, options, value, ...otherProps}) => (
    <Input type="select" id={id} name={name} value={value} {...otherProps}>
      {options.map((opt, index) => <option key={index}>{opt}</option>)}
    </Input>);

const SelectRow = ({label, id, name, options, ...otherProps}) => (<PlaygroundFormRow label={label} id={id}>
  <Select name={name} id={id} options={options} {...otherProps}/>
</PlaygroundFormRow>);

const RequestBodyInput = (textAreaProps) => {
  return <FormGroup>
    <Label for="requestBody">Request Body</Label>
    <Input id="requestBody" type="textarea" rows={10} {...textAreaProps}/>
  </FormGroup>;
};

export class PlaygroundForm extends React.Component<PlaygroundFormProps, PlaygroundFormState> {

  static defaultMime: string = 'application/json';
  static defaultMethod: ApiVerb = 'TRACE';

  constructor(props: PlaygroundFormProps) {
    super(props);
    this.state = PlaygroundForm.getInitialRequestInfo(props);
  }

  componentWillReceiveProps(nextProps: PlaygroundFormProps) {
    this.setState(PlaygroundForm.getInitialRequestInfo(nextProps));
  }

  static getInitialRequestInfo(props: PlaygroundFormProps): PlaygroundFormState {
    const doc: ApiMethodDoc = props.methodDoc;
    return {
      url: props.basePath + PlaygroundForm.getFirst(doc.path, ''),
      method: PlaygroundForm.getFirst(doc.verb, PlaygroundForm.defaultMethod),
      acceptHeader: PlaygroundForm.getFirst(doc.produces, PlaygroundForm.defaultMime),
      contentType: PlaygroundForm.getFirst(doc.consumes, PlaygroundForm.defaultMime),
      body: PlaygroundForm.getTemplateBody(doc.bodyobject),
    };
  }

  static getFirst<T>(arr: Array<T>, defaultValue: T): T {
    if (!arr) {
      return defaultValue;
    }
    return arr.length === 0 ? defaultValue : arr[0];
  }

  static getTemplateBody(obj: ?ApiBodyObjectDoc): string {
    const template = obj && obj.template;
    return template ? JSON.stringify(template, null, 2) : '';
  }

  handleChange(event: any) {
    const param = event.target.name;
    const value = event.target.value || event.target.checked;
    this.setState(prev => (Object.assign({}, prev, {[param]: value})));
  }

  handleSubmit(event: any) {
    event.preventDefault();
    const request: RequestInfo = {
      url: this.state.url,
      method: this.state.method,
      headers: {
        accept: this.state.acceptHeader,
        contentType: this.state.contentType,
      },
      body: this.state.body === '' ? undefined : this.state.body,
    };
    this.props.onSubmit(request);
  }

  render() {
    const doc = this.props.methodDoc;

    return <Form onSubmit={e => this.handleSubmit(e)}>
      <PlaygroundFormRow label="URL" id="urlInput">
        <Input type="text" id="urlInput" disabled value={this.state.url}/>
      </PlaygroundFormRow>
      <SelectRow label="Method" id="methodSelect" name="method" options={doc.verb}
                 value={this.state.method} onChange={e => this.handleChange(e)}/>
      <SelectRow label="Accept" id="acceptSelect" name="acceptHeader" options={doc.produces}
                 value={this.state.acceptHeader} onChange={e => this.handleChange(e)}/>
      <SelectRow label="Content Type" id="contentSelect" name="contentType" options={doc.consumes}
                 value={this.state.contentType} onChange={e => this.handleChange(e)}/>
      {doc.bodyobject && <RequestBodyInput name="body" value={this.state.body} onChange={e => this.handleChange(e)}/>}
      <Button type="submit">Submit</Button>
    </Form>;
  }
}
