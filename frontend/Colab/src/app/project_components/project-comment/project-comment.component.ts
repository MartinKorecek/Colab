import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ProjectComment } from '../../entity/project-comment';

@Component({
  selector: 'app-project-comment',
  templateUrl: './project-comment.component.html',
  styleUrls: ['./project-comment.component.css']
})
export class ProjectCommentComponent implements OnInit {
  @Input() commentData: ProjectComment;

  constructor() { }

  ngOnInit() {
  }

}
