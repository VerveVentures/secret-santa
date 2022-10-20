import { Component } from 'react';
import { toast } from "react-toastify";

export class alertsService extends Component {
  //documentation: https://fkhadra.github.io/react-toastify/introduction
  ShowSuccess(text) {
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

  showWarning(text) {
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

  showError(text) {
    toast.error(text || 'An unexpected error ocurred', {
      position: "top-right",
      autoClose: 10000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    })
  }
}