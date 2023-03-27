package dev.picco.portfolio.project.services;

import dev.picco.portfolio.project.controllers.requests.CreateProjectRequest;
import dev.picco.portfolio.project.controllers.responses.CreateProjectResponse;
import dev.picco.portfolio.project.controllers.responses.GetProjectInfoByIdResponse;
import dev.picco.portfolio.project.controllers.responses.GetProjectUrlByIdResponse;
import dev.picco.portfolio.project.domain.Project;
import dev.picco.portfolio.project.domain.ProjectInfo;
import dev.picco.portfolio.project.domain.ProjectUrl;
import dev.picco.portfolio.project.repositories.ProjectRepository;
import dev.picco.portfolio.tag.controllers.responses.GetTagByIdResponse;
import dev.picco.portfolio.tag.domain.Tag;
import dev.picco.portfolio.tag.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProjectService {

	private final ProjectRepository projectRepository;

	private final TagRepository tagRepository;

	public CreateProjectResponse execute(final Long projectId, final CreateProjectRequest request){

		Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("A project with ID " + projectId + " does not exist"));

		List<Long> missingTagsIds = tagRepository.findMissingTagIds(request.tagsIds());

		if (!missingTagsIds.isEmpty()) {
			throw new IllegalArgumentException("The tags with IDs " +
			                                   getMissingIdsSeparatedByComma(missingTagsIds) +
			                                   " does not exists");
		}

		List<Tag> newTags = tagRepository.findAllById(request.tagsIds());


		List<ProjectUrl> projectUrls = createProjectUrlsBasedInRequests(request);

		ProjectInfo newProjectInfo = ProjectInfo.of(request.getProjectImageUrl(), projectUrls);

		project.update(request.name(), request.description(), newTags, newProjectInfo);

		//TODO: Possible refactor - Insert responses generator in DTOs to avoid duplicate methods.

		return new CreateProjectResponse(project.getId(),
		                                 project.getName(),
		                                 project.getDescription(),
		                                 generateTagResponses(project),
		                                 generateProjectInfoResponse(project));
	}

	private String getMissingIdsSeparatedByComma(List<Long> missingTagsIds) {

		List<String> missingTagsIdsString = missingTagsIds.stream()
		                                                  .map(Object::toString)
		                                                  .toList();

		return String.join(", ", missingTagsIdsString);
	}

	private static List<ProjectUrl> createProjectUrlsBasedInRequests(final CreateProjectRequest request) {

		return request.getProjectUrlsRequests()
		              .stream()
		              .map(projectUrlRequest -> ProjectUrl.of(projectUrlRequest.url(),
		                                                      projectUrlRequest.urlDescription(),
		                                                      projectUrlRequest.urlType()))
		              .toList();
	}

	private List<GetTagByIdResponse> generateTagResponses(final Project savedProject) {

		return savedProject.getTags()
		                   .stream()
		                   .map(tag -> GetTagByIdResponse.of(tag.getId(), tag.getName(), tag.getBackgroundColor()))
		                   .toList();
	}

	private GetProjectInfoByIdResponse generateProjectInfoResponse(final Project savedProject) {

		List<GetProjectUrlByIdResponse> projectUrlsResponses = savedProject.getProjectInfo()
		                                                                   .getProjectUrls()
		                                                                   .stream()
		                                                                   .map(projectUrl -> GetProjectUrlByIdResponse.of(
				                                                                   projectUrl.getId(),
				                                                                   projectUrl.getUrl(),
				                                                                   projectUrl.getUrlDescription(),
				                                                                   projectUrl.getUrlType()))
		                                                                   .toList();

		return GetProjectInfoByIdResponse.of(savedProject.getProjectInfo()
		                                                 .getId(),
		                                     savedProject.getProjectInfo()
		                                                 .getImageUrl(), projectUrlsResponses);
	}
}
