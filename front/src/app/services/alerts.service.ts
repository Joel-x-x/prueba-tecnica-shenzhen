import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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

  confirm(message: string): Observable<boolean> {
    return new Observable<boolean>((observer) => {
      Swal.fire({
        title: "¿Estás seguro?",
        text: message,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
      }).then((result) => {
        observer.next(result.isConfirmed);
        observer.complete();
      });
    });
  }

}
