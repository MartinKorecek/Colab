import { Component, OnInit, Inject } from '@angular/core';
import { Project } from '../../entity/project';
import { ProjectService } from '../../service/project-service';

@Component({
  selector: 'app-timeline-content',
  templateUrl: './timeline-content.component.html',
  styleUrls: ['./timeline-content.component.css']
})
export class TimelineContentComponent implements OnInit {
  private projects: Project[];

  constructor(private projectService: ProjectService) { }

  ngOnInit() {
    this.projectService.getTimelineProjectCaptions().subscribe((p: Project[]) => this.projects = p);
  }

}
