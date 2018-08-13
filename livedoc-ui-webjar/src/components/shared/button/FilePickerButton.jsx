//@flow
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import * as React from 'react'
import { Button } from 'reactstrap';

export type FilePickerProps = {
  label: string,
  title: ?string,
  onChange: (file: File) => void,
  [string]: any
}

type FilePickerState = {
  fileInput: any,
}

export class FilePickerButton extends React.Component<FilePickerProps, FilePickerState> {

  fileInput: ?HTMLInputElement;

  triggerFilePicker = () => {
    this.fileInput && this.fileInput.click();
  };

  handleUpload = (evt: SyntheticEvent<HTMLInputElement>) => {
    const file = evt.currentTarget.files[0];
    this.props.onChange(file);

    // free up the fileInput again
    if (this.fileInput) {
      this.fileInput.value = "";
    }
  };

  render() {
    const {label, ...props} = this.props;
    return <div>
      <input type="file"
             style={{display: 'none'}}
             onChange={this.handleUpload}
             ref={ele => this.fileInput = ele}/>
      <Button {...props} onClick={this.triggerFilePicker}><FontAwesomeIcon icon="file-upload"
                                                                           style={{marginRight: '0.3rem'}}/> {label}
      </Button>
    </div>;
  }
}
