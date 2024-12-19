package com.megatronix.eden.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.megatronix.eden.enums.UserStatusEnum;
import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.User;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.service.IUserService;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Tag(name = "UserController", description = "Handles user-related operations including fetching, updating, and deleting user data.")
@Slf4j
public class UserController {
  @Resource
  private IUserService userService;

  @PostMapping("/authenticate")
  public ResultResponse<AuthUser> authenticateUser(@RequestBody UserAuthPayload userAuthPayload) {
    return userService.authenticateUser(userAuthPayload);
  }

  @PostMapping
  public ResultResponse<String> createAccount(@RequestBody UserAuthPayload userAuthPayload) {
    return userService.createAccount(userAuthPayload);
  }

  /**
   * Admin API to approve or reject a user's account.
   * 
   * @param uid               the user ID whose account status needs to be updated
   * @param newUserStatusEnum the new user status (ACTIVE, BANNED, etc.)
   * @return a response indicating whether the operation was successful
   */
  @Operation(summary = "Approve or Reject User Account", description = "Updates the status of a user's account, typically after admin review (e.g., from PENDING to ACTIVE or BANNED).")
  @PutMapping("/{uid}/status")
  public ResultResponse<String> setUserStatus(
      @Parameter(description = "The unique ID of the user whose status is to be updated.", required = true) @PathVariable String uid,
      @Parameter(description = "The new status to assign to the user (ACTIVE, BANNED, etc.).", required = true) @RequestParam UserStatusEnum newUserStatusEnum) {
    return userService.setUserStatus(uid, newUserStatusEnum);
  }

  @Operation(summary = "Get user details", description = "Allows the admin or user to fetch the details of a specific user.")
  @GetMapping("/{uid}")
  public ResultResponse<User> getUser(
      @Parameter(description = "User ID whose details are to be fetched.", required = true) @PathVariable String uid) {
    return userService.getUser(uid);
  }

  @Operation(summary = "Delete user account", description = "Allowsa an admin or the user themselves to delete the user account.")
  @DeleteMapping("/{uid}")
  public ResultResponse<String> deleteUser(
      @Parameter(description = "User ID whose account is to be deleted.", required = true) @PathVariable String uid) {
    return userService.deleteUser(uid);
  }

  @Operation(summary = "Get all users", description = "Fetches a list of all users in the system. Admins can view this.")
  @GetMapping
  public ResultResponse<List<User>> getUsers(
      @Parameter(description = "Page number for pagination (0-indexed). Defaults to 0 if not provided.", required = false) @PageableDefault(page = 0, size = 10, sort = "createAt") Pageable pageable) {

    return userService.getUsers(pageable);
  }

}
