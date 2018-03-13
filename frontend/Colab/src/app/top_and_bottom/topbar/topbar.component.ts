import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.css']
})
export class TopbarComponent implements OnInit {
  private profileIconTitle: string;
  private profileIconLink: string;

  constructor(private userService: UserService) {
    this.refreshProfileIconValues();

    this.userService.componentMethodCalled$.subscribe(     //update tooltip text of the profileIcon according to a signal from userService
      () => this.refreshProfileIconValues()
    );
  }

  refreshProfileIconValues() {
    let username = localStorage.getItem('authenticatedUsername');
    if (username != null) {
      this.profileIconTitle = "'" + username + "' profile and logout";
      this.profileIconLink = "/logout"
    } else {
      this.profileIconTitle = "Login or Sign Up";
      this.profileIconLink = "/login"
    }
  }

  ngOnInit() {
  }

}
