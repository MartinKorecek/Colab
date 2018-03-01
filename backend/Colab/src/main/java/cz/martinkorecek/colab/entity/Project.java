package cz.martinkorecek.colab.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="project")
public class Project {
	
	private long id;
	
	private User author;

	private String caption;

	private String description;
	
	private Date creationDate;
	
	private Set<ProjectComment> projectComments;
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@Size(max = 45)
	@Column(name="caption")
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="creation_date")
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	public Set<ProjectComment> getProjectComments() {
		return projectComments;
	}
	public void setProjectComments(Set<ProjectComment> projectComments) {
		this.projectComments = projectComments;
	}
	
	
	@Override
	public String toString() {
		return "Project [id=" + id + ", author=" + author + ", caption=" + caption + ", description=" + description
				+ ", creationDate=" + creationDate + ", projectComments=" + projectComments + "]";
	}

}
