package cz.martinkorecek.colab.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="project_description_chapter")
public class ProjectDescriptionChapter {

	private long id;
	
	private String title;
	
	private String text;
	
	private Project project;

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Column(name="title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@ManyToOne
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	
	@Override
	public String toString() {
		return "ProjectDescriptionChapter [id=" + id + ", title=" + title + ", text=" + text + ", project=" + project
				+ "]";
	}
	
}
