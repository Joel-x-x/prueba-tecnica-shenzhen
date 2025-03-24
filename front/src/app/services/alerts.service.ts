import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertsService {

  constructor() { }

  success(message: string) {
    Swal.fire({
      title: message,
      icon: "success",
      draggable: true
    });
  }

  error(message: string) {
    Swal.fire({
      title: message,
      icon: "error",
      draggable: true
    });
  }
}
