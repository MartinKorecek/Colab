package cz.martinkorecek.colab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import cz.martinkorecek.colab.entity.ProjectComment;
import cz.martinkorecek.colab.repository.ProjectRepository;
import cz.martinkorecek.colab.repository.UserRepository;
import cz.martinkorecek.colab.service.ProjectCommentService;

public class ProjectCommentServiceImpl implements ProjectCommentService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProjectRepository projectRepository;

	/*
	 * ensures the user and project that a comment binds to exist
	 * -actually unnecessarily calling the database twice instead of relying on an error that would appear anyway
	 * -might omit this step eventually
	 */
	@Override
	public boolean isValid(ProjectComment projectComment) {
		return userRepository.countByUsername(projectComment.getAuthor().getUsername()) == 1
				&& projectRepository.countById(projectComment.getProject().getId()) == 1;
	}

}
