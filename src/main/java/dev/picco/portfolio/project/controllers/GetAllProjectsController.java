package dev.picco.portfolio.project.controllers;

import dev.picco.portfolio.project.controllers.responses.GetProjectByIdResponse;
import dev.picco.portfolio.project.services.GetAllProjectsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "project")
public class GetAllProjectsController {

	private final GetAllProjectsService getAllProjectsService;

	@GetMapping("/v1/projects")
	public ResponseEntity<List<GetProjectByIdResponse>> execute(){

		var response = getAllProjectsService.execute();

		return ResponseEntity.status(200).body(response);
	}
}
