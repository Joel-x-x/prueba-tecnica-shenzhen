import { NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from '../../auth/service/auth.service';
import { IRole, IUser, IUserResponse } from '../user/interfaces/user.interface';

@Component({
  selector: 'app-sidebar',
  imports: [RouterOutlet, NgClass],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  currentTab: string = '';
  user: IUser = {
    id: "",
    email: "",
    firstNames: "",
    lastNames: "",
    roles: [
      {
        id: "",
        name: ""
      }
    ]
  };

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.getRoute();
    this.authService.getUser().subscribe({
      next: response => {
        this.user = response.result;
        console.log(this.user);
      }
    });
  }

  toggleSidebar() {
    const sidebar = document.getElementById('default-sidebar');
    if (sidebar) {
      sidebar.classList.toggle('-translate-x-full');
    }
  }

  getRoute() {
    const path = window.location.pathname.split('/');
    this.currentTab = path[path.length - 1];
  }

  isActivated(tab: string) {
    return tab === this.currentTab;
  }

  nagivateTo(tab: string) {
    this.router.navigate([`/admin/${tab}`]);
    setTimeout(() => {
      this.getRoute();
      this.toggleSidebar();
    }, 200)
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('id');
    this.router.navigate(['/auth/login']);
  }
}
