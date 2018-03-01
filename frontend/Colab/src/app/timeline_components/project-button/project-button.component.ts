import { Component, OnInit, Input } from '@angular/core';
import { Project } from '../../entity/project';

@Component({
  selector: 'app-project-button',
  templateUrl: './project-button.component.html',
  styleUrls: ['./project-button.component.css']
})
export class ProjectButtonComponent implements OnInit {
  @Input() project: Project;

  constructor() { }

  ngOnInit() {
  }

}
