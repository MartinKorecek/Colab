import { Project } from "./project";

export class ProjectDescriptionChapter {
    id: number;
    title: string;
    text: string;
    project: Project;
    expanded: boolean = false;   //not a database column; useful in project-content.component

    constructor (title: string, text: string, projectId?: number) {
        this.title = title;
        this.text = text;
        this.project = new Project(projectId);
    }

}