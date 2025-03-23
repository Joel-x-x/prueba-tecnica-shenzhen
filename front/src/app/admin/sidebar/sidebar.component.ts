import { NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterOutlet, NgClass],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  currentTab: string = '';


  constructor(private router: Router
  ) {}

  ngOnInit() {
    this.getRoute();
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
    }, 200)
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('id');
    this.router.navigate(['/auth/login']);
  }
}
