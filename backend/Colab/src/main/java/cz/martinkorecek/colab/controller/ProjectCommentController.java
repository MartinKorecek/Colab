package cz.martinkorecek.colab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cz.martinkorecek.colab.entity.ProjectComment;
import cz.martinkorecek.colab.repository.ProjectCommentRepository;
import cz.martinkorecek.colab.service.ProjectCommentService;

@RestController
@RequestMapping("/projectComment")
public class ProjectCommentController {

	@Autowired
	private ProjectCommentService projectCommentService;

	@Autowired
	private ProjectCommentRepository projectCommentRepository;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('STANDARD_USER') and #projectComment.author.getUsername() == principal")
	@RequestMapping(value = "/persistComment", method = RequestMethod.POST)
	public ResponseEntity<String> add(@RequestBody ProjectComment projectComment) {
		if (projectCommentService.isValid(projectComment)) {
			if (projectComment.getParentComment() == null || projectComment.getParentComment().getId() == null) {
				projectCommentRepository.insertComment(projectComment.getAuthor().getUsername(), projectComment.getText(), projectComment.getProject().getId());
			} else {
				projectCommentRepository.insertSubcomment(projectComment.getAuthor().getUsername(), projectComment.getText(), projectComment.getProject().getId(), projectComment.getParentComment().getId());
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

}
