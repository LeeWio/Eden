package com.megatronix.eden.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.megatronix.eden.enums.UserStatusEnum;
import com.megatronix.eden.pojo.AuthUser;
import com.megatronix.eden.pojo.User;
import com.megatronix.eden.pojo.UserAuthPayload;
import com.megatronix.eden.util.ResultResponse;

import cn.hutool.http.server.HttpServerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Tag(name = "User Service", description = "Service to handle user authentication and account management")
public interface IUserService {

  /**
   * Request a verification code for the provided email address.
   * This API will send a verification code to the user's email.
   * 
   * @param email the email address to which the verification code will be sent
   * @return a response indicating the status of the operation
   */
  @Operation(summary = "Request Verification Code", description = "This API sends a verification code to the provided email address. It is commonly used for operations like password recovery or account verification.")
  ResultResponse<String> requestVerificationCode(
      @Parameter(description = "The email address to which the verification code will be sent", required = true) String email);

  /**
   * Validate the captcha entered by the user for the provided email address.
   * This API will verify the captcha code entered by the user. If the captcha is
   * valid,
   * the system will return the authenticated user details.
   * 
   * @param email            the email address associated with the captcha request
   * @param verificationCode the captcha code entered by the user for verification
   * @return a response containing the authenticated user details if the captcha
   *         is valid,
   *         or an error message if the captcha is invalid or the user cannot be
   *         found
   */
  // @Operation(summary = "Validate Captcha", description = "This API validates
  // the captcha entered by the user for the specified email address. "
  // + "If the captcha is correct, the user details will be returned for further
  // authentication. "
  // + "It is commonly used for user login or multi-factor authentication.")
  // ResultResponse<AuthUser> validateCaptcha(
  // @Parameter(description = "The email address associated with the captcha
  // verification", required = true) String email,
  // @Parameter(description = "The captcha code entered by the user", required =
  // true) String verificationCode);

  /**
   * Authenticates a user using the provided authentication details.
   * 
   * @param userAuthPayload the user's authentication details (email and password)
   * @return ResultResponse containing authenticated user information
   */
  @Operation(summary = "Authenticate User", description = "Authenticates the user using the provided credentials.")
  ResultResponse<AuthUser> authenticateUser(
      @Parameter(description = "User authentication payload containing email and password", required = true) UserAuthPayload userAuthPayload,
      HttpServletRequest httpServletRequest);

  /**
   * Creates a new user account based on the provided user data.
   * 
   * @param userAuthPayload the user registration data (email, password, etc.)
   * @return ResultResponse containing the result of the account creation
   *         operation
   */
  @Operation(summary = "Create User Account", description = "Creates a new user account based on the provided details.")
  ResultResponse<AuthUser> createAccount(
      @Parameter(description = "User authentication payload containing registration information", required = true) UserAuthPayload userAuthPayload,
      HttpServletRequest httpServerRequest);

  /**
   * Approves a user account by changing its status based on the admin's decision.
   *
   * @param uid            the user ID whose account is to be approved
   * @param userStatusEnum the new status of user account is to be updated
   * @return ResultResponse containing the result of the account approval
   *         operation
   */
  @Operation(summary = "Approve User Account", description = "Approves a user account and changes its status to active after admin review.")
  ResultResponse<String> setUserStatus(
      @Parameter(description = "The unique ID of the user account to approve", required = true) String uid,
      @Parameter(description = "The new status to set for the user account after approval", required = true, schema = @Schema(implementation = UserStatusEnum.class)) UserStatusEnum newUserStatusEnum);

  /**
   * Retrieves a user's details by their unique user ID.
   * 
   * @param uid the unique ID of the user whose details are to be retrieved
   * @return ResultResponse containing the user details if found
   */
  @Operation(summary = "Get User Details", description = "Fetches the details of a user by their unique ID. This API can be used by the admin or the user themselves to retrieve their profile information.")
  ResultResponse<User> getUser(
      @Parameter(description = "The unique ID of the user whose details are to be fetched", required = true) String uid);

  @Operation(summary = "Delete User", description = "Deletes a user from the system based on the provided UID.")
  ResultResponse<String> deleteUser(
      @Parameter(description = "The unique ID of the user to delete", required = true) String uid);

  /**
   * Retrieves a paginated list of users.
   * 
   * @param pageable the pagination information (contains the page number and
   *                 size)
   * @return ResultResponse containing a list of users for the requested page
   */
  @Operation(summary = "Get User List", description = "Fetches a paginated list of users from the system. The results are returned based on the provided pagination details.")
  ResultResponse<List<User>> getUsers(
      @Parameter(description = "Pagination information including page number (0-indexed) and page size.", required = true) Pageable pageable);

  /**
   * Updates the last login IP address for the specified user.
   *
   * @param uid         the unique ID of the user
   * @param lastLoginIp the new IP address to be set as the user's last login IP
   * @return a response indicating whether the update was successful
   */
  @Operation(summary = "Update Last Login IP", description = "Updates the last login IP address of a user. This is typically called after a successful login to record the login IP.")
  ResultResponse<String> updateLastLoginIp(
      @Parameter(description = "The unique ID of the user whose last login IP is to be updated", required = true) String uid,

      @Parameter(description = "The new IP address to set as last login IP", required = true) String lastLoginIp);
}
