import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../service/project-service';
import { Project } from '../../entity/project';
import { ActivatedRoute, Router } from '@angular/router';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { UserService } from '../../service/user-service';
import { ProjectCommentService } from '../../service/project_comment-service';
import { ProjectComment } from '../../entity/project-comment';
import { error } from 'util';

@Component({
  selector: 'app-project-content',
  templateUrl: './project-content.component.html',
  styleUrls: ['./project-content.component.css']
})
export class ProjectContentComponent implements OnInit, OnDestroy {
  private model: any = {};
  private receivedId: number;
  private sub: any;
  private commentPersistenceError: boolean = false;

  private project: Project;
  private username: string;

  constructor(private projectService: ProjectService, private route: ActivatedRoute, private router: Router
    , private userService: UserService, private projectCommentService: ProjectCommentService) {
    this.username = localStorage.getItem('authenticatedUsername');
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.receivedId = +params['id'];
    });

    if (isNaN(this.receivedId)) {
      this.router.navigateByUrl("**", {skipLocationChange: true});
      return;
    }

    this.projectService.getProjectData(this.receivedId).subscribe(
      (p: Project) => {
        this.project = p;
        if (this.project == null) {
          this.router.navigateByUrl("**", {skipLocationChange: true});
        }
      },
      error => this.router.navigateByUrl("connectionError", {skipLocationChange: true})
    );
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  postComment() {
    let commentText = this.model.commentText;
    this.projectCommentService.postComment(new ProjectComment(this.username, commentText, this.project.id))
      .subscribe(
        data => {
          this.commentPersistenceError = false;
          this.project.comments.push(new ProjectComment(this.username, commentText));
          this.model.commentText = '';
        },
        err => this.commentPersistenceError = true
      );
  }

  expandChapter(index: number) {
    this.project.projectDescriptionChapters[index].expanded = true;
  }

  collapseChapter(index: number) {
    this.project.projectDescriptionChapters[index].expanded = false;
  }

}
