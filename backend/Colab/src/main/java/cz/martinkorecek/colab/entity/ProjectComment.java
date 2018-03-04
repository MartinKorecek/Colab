package cz.martinkorecek.colab.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="project_comment")
public class ProjectComment {

	private Long id;
	
	private String text;
	
	private Date date;
	
	private User author;
	
	private Project project;
	
	private ProjectComment parentComment;
	
	private Set<ProjectComment> subcomments;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@Column(name="date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User user) {
		this.author = user;
	}
	
	@ManyToOne
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "parent_comment_id", referencedColumnName="id", insertable = false, updatable = false)
	public ProjectComment getParentComment() {
		return parentComment;
	}
	public void setParentComment(ProjectComment parentComment) {
		this.parentComment = parentComment;
	}
	
	@OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<ProjectComment> getSubcomments() {
		return subcomments;
	}
	public void setSubcomments(Set<ProjectComment> subcomments) {
		this.subcomments = subcomments;
	}
	
	
	@Override
	public String toString() {
		return "ProjectComment [id=" + id + ", text=" + text + ", date=" + date + ", author=" + author + ", project="
				+ project + ", parentComment=" + parentComment + ", subcomments=" + subcomments + "]";
	}
	
}
