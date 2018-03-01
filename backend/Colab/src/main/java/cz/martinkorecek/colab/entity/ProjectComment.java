package cz.martinkorecek.colab.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="project_comment")
public class ProjectComment {

	private long id;
	
	private String text;
	
	private long parentCommentId;
	
	private Date date;
	
	private User author;
	
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

	@Column(name="text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@Column(name="parent_comment_id")
	public long getParentCommentId() {
		return parentCommentId;
	}
	public void setParentCommentId(long parentCommentId) {
		this.parentCommentId = parentCommentId;
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
	
	
	@Override
	public String toString() {
		return "ProjectComment [id=" + id + ", text=" + text + ", parentCommentId=" + parentCommentId + ", date=" + date
				+ ", author=" + author + ", project=" + project + "]";
	}
	
}
