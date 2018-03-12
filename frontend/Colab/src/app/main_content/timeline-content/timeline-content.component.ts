import { Component, OnInit, Inject } from '@angular/core';
import { Project } from '../../entity/project';
import { ProjectService } from '../../service/project-service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-timeline-content',
  templateUrl: './timeline-content.component.html',
  styleUrls: ['./timeline-content.component.css']
})
export class TimelineContentComponent implements OnInit {
  private projects: Project[];
  private sub: any;

  private totalCount: number;           //total amount of projects
  private timelineOrder: number = 0;    //which page we are currently at (starts from 0)
  private maxOrder: number = 0;

  private projectCountPerPage: number = 25;

  constructor(private projectService: ProjectService, private activatedRoute: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.sub = this.activatedRoute.params.subscribe(params => {
      this.timelineOrder = +params['order'];
    })

    if (isNaN(this.timelineOrder)) {
      this.timelineOrder = 0;
      this.router.navigateByUrl("timeline/" + 0);
    }
    this.updateProjectsByOrder(this.timelineOrder);
  }

  updateProjectsByOrder(order: number) {
    this.projectService.getTimelineProjectCaptions(order).subscribe(
      (p: Project[]) => {
        this.projects = p;
        if (this.projects.length == 0) {
          if (this.timelineOrder != 0) {
            this.router.navigateByUrl("timeline/" + 0);
            this.timelineOrder = 0;
            this.updateProjectsByOrder(0);
            return;
          }
          this.totalCount = 0;
        } else {
          this.totalCount = this.projects[0].totalCount;
          this.maxOrder = Math.ceil(this.totalCount / this.projectCountPerPage) - 1;
        }
      },
      error => {
        if (error.status == 0 || this.timelineOrder == 0) {
          this.router.navigateByUrl("connectionError", {skipLocationChange: true});
        } else {
          this.router.navigateByUrl("timeline/" + 0);
          this.timelineOrder = 0;
          this.updateProjectsByOrder(0);
        }
      }
    );
  }

  previousPage() {
    if (this.timelineOrder <= 0) {
      return;
    }

    this.updateProjectsByOrder(this.timelineOrder - 1);
    this.timelineOrder--;
    this.router.navigateByUrl("timeline/" + this.timelineOrder);
  }
  
  nextPage() {
    if (this.timelineOrder >= this.maxOrder) {
      return;
    }
    
    this.updateProjectsByOrder(this.timelineOrder + 1);
    this.timelineOrder++;
    this.router.navigateByUrl("timeline/" + this.timelineOrder);
  }

}
