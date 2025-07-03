package com.example.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.constants.MembershipMessages;
import com.example.taskmanager.response.ApiResponse;
import com.example.taskmanager.services.ProjectMembershipService;

@RestController
@RequestMapping("api/memberships/")
public class ProjectMembershipController {
    @Autowired
    private ProjectMembershipService projectMembershipService;

    @PostMapping("/invite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> invite(@RequestParam Long projectId, @RequestParam String username) {
        String result = projectMembershipService.invite(projectId, username);
        boolean success = result.equals("Success");
        if (success) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Invited successfuly", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, result, null));
        }

    }

    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<?>> request(@RequestParam Long projectId) {
        String result = projectMembershipService.request(projectId);
        boolean success = result.equals("Success");
        if (success) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Requested successfuly", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, result, null));
        }

    }

    @PostMapping("/requests/{membershipId}/accept")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<?>> userAccept(@PathVariable Long membershipId) {
        return buildResponse(projectMembershipService.userAccept(membershipId), MembershipMessages.ACCEPTED);
    }

    @PostMapping("/requests/{membershipId}/reject")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<?>> userReject(@PathVariable Long membershipId) {
        return buildResponse(projectMembershipService.userReject(membershipId), MembershipMessages.REJECTED);
    }

    @PostMapping("/invites/{membershipId}/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> adminAccept(@PathVariable Long membershipId) {
        return buildResponse(projectMembershipService.adminAccept(membershipId), MembershipMessages.ACCEPTED);
    }

    @PostMapping("/invites/{membershipId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> adminReject(@PathVariable Long membershipId) {
        return buildResponse(projectMembershipService.adminReject(membershipId), MembershipMessages.REJECTED);
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllProjectMemberships(@PathVariable Long projectId, @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok(new ApiResponse<>(true, "",
                projectMembershipService.getAllProjectMemberships(projectId, page, size, sortBy)));
    }

    private ResponseEntity<ApiResponse<?>> buildResponse(String result, String expectedSuccessMsg) {
        boolean success = result.equals(expectedSuccessMsg);
        return ResponseEntity.status(success ? 200 : 400)
                .body(new ApiResponse<>(success, result, null));
    }
}
