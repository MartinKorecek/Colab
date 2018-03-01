import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../service/project-service';
import { Project } from '../../entity/project';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-project-content',
  templateUrl: './new-project-content.component.html',
  styleUrls: ['./new-project-content.component.css']
})
export class NewProjectContentComponent implements OnInit {
  private model: any = { };
  private projectPersistenceError: boolean = false;

  private username: string;

  constructor(private router: Router, private projectService: ProjectService) {
    this.username = localStorage.getItem('authenticatedUsername');
  }

  ngOnInit() {
  }

  submitProject() {
    let caption = this.model.newProjectCaption;
    let description = this.model.newProjectDescription;
    this.projectService.postProject(new Project(null, null, this.username, caption, description))
    .subscribe(
      data => {
        this.projectPersistenceError = false;
        this.router.navigate(['/']);
      },
      err => this.projectPersistenceError = true
    );
  }

}
