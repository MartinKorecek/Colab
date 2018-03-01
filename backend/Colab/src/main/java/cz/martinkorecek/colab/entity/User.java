package cz.martinkorecek.colab.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	
	private long id;
	
	private String username;
	
	private String passwordHash;
	
	private Set<ProjectComment> projectComment;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="password_hash")
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	public Set<ProjectComment> getProjectComment() {
		return projectComment;
	}
	public void setProjectComment(Set<ProjectComment> projectComment) {
		this.projectComment = projectComment;
	}
	
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", passwordHash=" + passwordHash + ", projectComment="
				+ projectComment + "]";
	}
	
}
