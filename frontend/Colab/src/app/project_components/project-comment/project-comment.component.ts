import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ProjectComment } from '../../entity/project-comment';
import { ProjectCommentService } from '../../service/project_comment-service';

@Component({
  selector: 'app-project-comment',
  templateUrl: './project-comment.component.html',
  styleUrls: ['./project-comment.component.css']
})
export class ProjectCommentComponent implements OnInit {
  @Input() commentData: ProjectComment;
  @Input() projectAuthorName: string;

  private model: any = {};
  private username: string;
  private expandCommenting: boolean = false;
  private commentingControlText: string;
  private subcommentPersistenceError: boolean = false;

  constructor(private projectCommentService: ProjectCommentService) {
    this.username = localStorage.getItem('authenticatedUsername');
    this.commentingControlText = 'Post a reply';
  }

  ngOnInit() {
  }

  postSubcomment() {
    let subcommentText = this.model.subcommentText;
    this.projectCommentService.postComment(new ProjectComment(this.username, subcommentText, this.commentData.project.id, this.commentData.id))
      .subscribe(
        data => {
          this.subcommentPersistenceError = false;
          if (this.commentData.subcomments == null) {
            this.commentData.subcomments = [];
          }
          this.commentData.subcomments.push(new ProjectComment(this.username, subcommentText));
          this.model.subcommentText = '';
          this.expandCommenting = false;
          this.commentingControlText = 'Post a reply';
        },
        err => this.subcommentPersistenceError = true
      );
  }

  switchCommentingState() {
    if (this.expandCommenting) {
      this.expandCommenting = false;
      this.commentingControlText = 'Post a reply';
    } else {
      this.expandCommenting = true;
      this.commentingControlText = 'Collapse'
    }
  }

}
