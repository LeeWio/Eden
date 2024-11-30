//
//  JwtAuthenticationMiddleware.swift
//  Eden
//
//  Created by 李伟 on 2024/11/24.

import Fluent
import Vapor

// Create a Key for storing and retrieving the UserPayload from the request storage
struct UserPayloadKey: StorageKey {
    typealias Value = UserPayload
}

// Middleware for JWT authentication
struct JwtAuthenticationMiddleware: AsyncMiddleware {
    // Main method to handle the request and check for JWT in Authorization header
    func respond(to request: Request, chainingTo next: AsyncResponder) async throws -> Response {
        // Allow public routes to skip authentication
        let path = request.url.path
        if path == "/auth/authenticate" || path == "/auth/create" {
            return try await next.respond(to: request)
        }

        let authorization = getTokenFromHeaders(request)

        // Check if a token exists in the Authorization header
        if let token = authorization, !token.isEmpty {
            do {
                // Decode the token into your UserPayload struct
                let jwt = try await request.jwt.verify(token, as: UserPayload.self)

                // Print the decoded JWT payload (can be removed in production)
                request.logger.info("Authorization successful: \(jwt)")

                // Optionally, you can also attach the decoded payload to the request for further use
                request.storage[UserPayloadKey.self] = jwt

            } catch {
                // If token is invalid or verification fails, return a 401 Unauthorized response
                request.logger.error("Invalid or expired authorization token: \(error)")
                throw Abort(.unauthorized, reason: "Invalid or expired authorization token.")
            }
        } else {
            // If no token is found, return a 401 Unauthorized response
            request.logger.error("Missing authorization token")
            throw Abort(.unauthorized, reason: "Missing authorization token.")
        }

        // Call the next middleware or route handler
        let response = try await next.respond(to: request)
        return response
    }

    // Helper method to extract the JWT token from the Authorization header
    func getTokenFromHeaders(_ request: Request) -> String? {
        // Look for the Authorization header in the request
        if let bearerToken = request.headers.first(where: { $0.0 == "Authorization" })?.1 {
            // Check if the token starts with "Bearer "
            if !bearerToken.isEmpty, bearerToken.starts(with: "Bearer ") {
                // Remove the "Bearer " prefix and return the token part
                return String(bearerToken.dropFirst(7)) // Remove "Bearer " part
            }
        }
        // Return nil if the Authorization header is not found or is malformed
        return nil
    }
}
