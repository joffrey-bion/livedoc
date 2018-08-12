import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import './IconButton.css'

export const IconButton = ({icon, onClick, ...props}) => (
        <span onClick={onClick} {...props}>
          <FontAwesomeIcon icon={icon} className='icon'/>
        </span>);
