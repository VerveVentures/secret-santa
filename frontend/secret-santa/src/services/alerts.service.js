import { Component } from 'react';
import { toast } from "react-toastify";

export class AlertsService extends Component {
  //documentation: https://fkhadra.github.io/react-toastify/introduction
  success(text) {
    toast.success(text, {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    })
  }

  warn(text) {
    toast.warn(text, {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    })
  }

  error(text) {
    toast.error(text || 'An unexpected error ocurred.', {
      position: "top-right",
      autoClose: 100000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    })
  }
}