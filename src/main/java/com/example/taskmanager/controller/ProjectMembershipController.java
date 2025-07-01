package com.example.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.response.ApiResponse;
import com.example.taskmanager.services.ProjectMembershipService;

@RestController
@RequestMapping("api/memberships/")
public class ProjectMembershipController {
    @Autowired
    private ProjectMembershipService projectMembershipService;

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<?>> invite(@RequestParam Long projectId, @RequestParam Long userId) {
        String resualt = projectMembershipService.invite(projectId, userId);
        if (resualt.equals("Success")) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Invited Sucessfuly", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, resualt, null));
        }

    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<?>> request(@RequestParam Long projectId, @RequestParam Long userId) {
        String resualt = projectMembershipService.request(projectId, userId);
        if (resualt.equals("Success")) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Requested Sucessfuly", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, resualt, null));
        }

    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<?>> getAllProjectMemberships(@PathVariable Long projectId, @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok(new ApiResponse<>(true, "",
                projectMembershipService.getAllProjectMemberships(projectId, page, size, sortBy)));
    }
}
