package dev.picco.portfolio.project.services;

import dev.picco.portfolio.project.controllers.responses.GetProjectByIdResponse;
import dev.picco.portfolio.project.controllers.responses.GetProjectInfoByIdResponse;
import dev.picco.portfolio.project.controllers.responses.GetProjectUrlByIdResponse;
import dev.picco.portfolio.project.domain.Project;
import dev.picco.portfolio.project.repositories.ProjectRepository;
import dev.picco.portfolio.tag.controllers.responses.GetTagByIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProjectByIdService {

	private final ProjectRepository projectRepository;

	public GetProjectByIdResponse execute(Long projectId) {

		Project project = projectRepository.findById(projectId)
		                                   .orElseThrow(() -> new IllegalArgumentException("A project with ID " +
		                                                                                   projectId +
		                                                                                   " does not exist."));

		return GetProjectByIdResponse.of(project.getId(),
		                                 project.getName(),
		                                 project.getDescription(),
		                                 getProjectTagsResponses(project),
		                                 getProjectInfoResponse(project));
	}

	private static List<GetTagByIdResponse> getProjectTagsResponses(Project project) {

		return project.getTags()
		              .stream()
		              .map(projectTag -> GetTagByIdResponse.of(projectTag.getId(),
		                                                       projectTag.getName(),
		                                                       projectTag.getBackgroundColor()))
		              .toList();
	}

	private static GetProjectInfoByIdResponse getProjectInfoResponse(Project project) {

		return GetProjectInfoByIdResponse.of(project.getProjectInfo()
		                                            .getId(),
		                                     project.getProjectInfo()
		                                            .getImageUrl(),
		                                     getProjectUrlsResponses(project));
	}

	private static List<GetProjectUrlByIdResponse> getProjectUrlsResponses(Project project) {

		return project.getProjectInfo()
		              .getProjectUrls()
		              .stream()
		              .map(projectUrl -> GetProjectUrlByIdResponse.of(projectUrl.getId(),
		                                                              projectUrl.getUrl(),
		                                                              projectUrl.getUrlDescription(),
		                                                              projectUrl.getUrlType()))
		              .toList();
	}
}