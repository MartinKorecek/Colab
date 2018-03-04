package cz.martinkorecek.colab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.martinkorecek.colab.entity.ProjectDescriptionChapter;

@Repository
public interface ProjectDescriptionChapterRepository extends JpaRepository<ProjectDescriptionChapter, Long> {

}
