import { Component, OnInit, Inject } from '@angular/core';
import { Project } from '../../entity/project';
import { ProjectService } from '../../service/project-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-timeline-content',
  templateUrl: './timeline-content.component.html',
  styleUrls: ['./timeline-content.component.css']
})
export class TimelineContentComponent implements OnInit {
  private projects: Project[];

  constructor(private projectService: ProjectService, private router: Router) { }

  ngOnInit() {
    this.projectService.getTimelineProjectCaptions().subscribe(
      (p: Project[]) => this.projects = p,
      err => this.router.navigateByUrl("connectionError", {skipLocationChange: true})
    );
  }

}
