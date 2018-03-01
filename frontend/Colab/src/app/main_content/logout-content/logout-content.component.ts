import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout-content',
  templateUrl: './logout-content.component.html',
  styleUrls: ['./logout-content.component.css']
})
export class LogoutContentComponent implements OnInit {
  private username: string;

  constructor(private router: Router, private userService: UserService) {
    this.username = localStorage.getItem('authenticatedUsername')
  }

  ngOnInit() {
  }

  logout() {
    this.userService.logout();
    this.router.navigate(['/']);
  }

}
