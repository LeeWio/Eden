package com.megatronix.eden.controller;

import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.User;
import com.megatronix.eden.enums.UserStatusEnum;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.service.IUserService;
import com.megatronix.eden.util.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;

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

@RestController
@RequestMapping("/auth")
@Tag(name = "UserController", description = "Handles user-related operations including fetching, updating, and deleting user data.")
@Slf4j
public class UserController {
  @Resource
  private IUserService userService;

  @PostMapping("/authenticate")
  public ResultResponse<AuthUser> authenticateUser(@RequestBody UserAuthPayload userAuthPayload,
      HttpServletRequest httpServerRequest) {
    return userService.authenticateUser(userAuthPayload, httpServerRequest);
  }

  @PostMapping
  public ResultResponse<AuthUser> createAccount(@RequestBody UserAuthPayload userAuthPayload,
      HttpServletRequest httpServerRequest) {
    return userService.createAccount(userAuthPayload, httpServerRequest);
  }

  /**
   * Validate the captcha entered by the user for a given email.
   * This API verifies the captcha code entered by the user. If the captcha is
   * correct,
   * the system will return the corresponding authenticated user details.
   * 
   * @param email            the email address associated with the captcha request
   * @param verificationCode the captcha code entered by the user
   * @return a response containing the authenticated user details if the captcha
   *         is valid,
   *         or an error message if the captcha is invalid
   * 
   * @Operation(summary = "Validate Captcha", description = "This API validates
   *                    the captcha entered by the user for the specified email
   *                    address."
   *                    + " If the captcha is correct, the user details will be
   *                    returned for further authentication.")
   * @ApiResponses(value = {
   * @ApiResponse(responseCode = "200", description = "Captcha validated
   *                           successfully, user details returned"),
   * @ApiResponse(responseCode = "400", description = "Invalid captcha code or
   *                           email address"),
   * @ApiResponse(responseCode = "404", description = "User not found for the
   *                           provided email address"),
   * @ApiResponse(responseCode = "500", description = "Internal server error
   *                           during captcha validation")
   *                           })
   */
  // @Operation(summary = "Validate Captcha", description = "This API validates
  // the captcha entered by the user for the specified email address. "
  // + "If the captcha is correct, the user details will be returned for further
  // authentication.")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "Captcha validated
  // successfully, user details returned"),
  // @ApiResponse(responseCode = "400", description = "Invalid captcha code or
  // email address"),
  // @ApiResponse(responseCode = "404", description = "User not found for the
  // provided email address"),
  // @ApiResponse(responseCode = "500", description = "Internal server error
  // during captcha validation")
  // })
  // @PostMapping("/validateCaptcha/{email}/{verificationCode}")
  // public ResultResponse<AuthUser> validateCaptcha(
  // @PathVariable("email") String email,
  // @PathVariable("verificationCode") String verificationCode) {
  // return userService.validateCaptcha(email, verificationCode);
  // }

  /**
   * Request a verification code for the provided email address.
   * This API will send a verification code to the user's email.
   * 
   * @param email the email address to which the verification code will be sent
   * @return a response indicating the status of the operation
   */
  @Operation(summary = "Request Verification Code", description = "This API sends a verification code to the provided email address. "
      + "It is commonly used for operations like password recovery or account verification.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Verification code sent successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid email address format"),
      @ApiResponse(responseCode = "500", description = "Failed to send verification code due to server error")
  })
  @PostMapping("/requestVerificationCode/{email}")
  public ResultResponse<String> requestVerificationCode(
      @Parameter(description = "The email address to which the verification code will be sent", required = true) @PathVariable @Email String email) {
    return userService.requestVerificationCode(email);
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
