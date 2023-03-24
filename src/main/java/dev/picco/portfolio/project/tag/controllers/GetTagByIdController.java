package dev.picco.portfolio.project.tag.controllers;

import dev.picco.portfolio.project.tag.controllers.responses.GetTagByIdResponse;
import dev.picco.portfolio.project.tag.services.GetTagByIdService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetTagByIdController {

	private final GetTagByIdService getTagByIdService;

	@GetMapping("/tag/{tagId}")
	public ResponseEntity<GetTagByIdResponse> execute(@NotNull @PathVariable final Long tagId){

		var response = getTagByIdService.execute(tagId);

		return ResponseEntity.ok(response);
	}
}
