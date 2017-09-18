import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import * as React from 'react';

export class DocFetcher extends React.Component {

  constructor(props) {
    super(props);
    this.state = {url: ''};
  }

  setUrl(url) {
    this.setState({url});
  };

  render() {
    return <div>
      <TextField type='text' name='url' hintText='URL to JSON documentation' onChange={(e, url) => this.setUrl(url)}/>
      <RaisedButton label='Fetch' primary onClick={ () => this.props.fetch(this.state.url)}/>
    </div>
  }
}
