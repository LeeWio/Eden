//
//  File.swift
//  
//
//  Created by 李伟 on 2024/10/27.
//

import Fluent
import Vapor

struct LoginRequest: Content {
    let email: String
    let password: String
}

extension LoginRequest: Validatable {
    static func validations(_ validations: inout Validations) {
        // Validations go here.
        validations.add("email", as: String.self, is: .email)
    }
}

struct UserRegistrationRequest: Content {
    let email: String
    let password: String
}

extension UserRegistrationRequest: Validatable {
    static func validations(_ validations: inout Validations) {
        validations.add("email", as: String.self, is: .email, required: true)
        validations.add("password", as: String.self, is: .count(8...), required: true)
    }
}

final class UserController: RouteCollection {
    func boot(routes: any RoutesBuilder) throws {
        let users = routes.grouped("auth")
        users.post("register", use: create)
        users.post("authenticate", use: authenticateUser)
        users.get("getAll", use: getAll)
    }

    // User registration function
    func create(req: Request) async throws -> ResultResponse<String> {
        try UserRegistrationRequest.validate(content: req)  // Validate input

        let registerUser = try req.content.decode(UserRegistrationRequest.self)  // Decode request data

        // Ensure email and password are not empty
        guard !registerUser.email.isEmpty, !registerUser.password.isEmpty else {
            return ResultResponse(resultStatusEnum: .badRequest, data: "Email and password are required.")
        }

        // Check if the email already exists in the database
        let existingUser = try await UserModel.query(on: req.db)
            .filter(\.$email == registerUser.email)
            .first()

        // If the user already exists, return an error
        guard existingUser == nil else {
            return ResultResponse(resultStatusEnum: .badRequest, data: "Email is already registered.")
        }

        // Create a new user and hash the password
        let user = try UserModel(
            email: registerUser.email,
            passwordHash: Bcrypt.hash(registerUser.password)
        )
        
        // Save the user in the database
        try await user.save(on: req.db)

        // Assign default role "USER" to the new user
        guard let defaultRole = try await RoleModel.query(on: req.db)
            .filter(\.$name == "USER")
            .first() else {
                throw Abort(.internalServerError, reason: "Default role not found.")
        }
        
        // Assign "read" permission to the new user
        guard let readPermission = try await PermissionModel.query(on: req.db)
            .filter(\.$name == "read")
            .first() else {
                throw Abort(.internalServerError, reason: "Default permission not found.")
        }
        
        // Attach the role and permission to the user
        try await user.$roles.attach(defaultRole, on: req.db)
        try await user.$permissions.attach(readPermission, on: req.db)

        return ResultResponse(resultStatusEnum: .success, data: "Registration successful")
    }


    // User login function to authenticate the user and generate a JWT token
    func authenticateUser(req: Request) async throws -> ResultResponse<AuthUser> {

        try LoginRequest.validate(content: req)  // Validate input
        
        let loginRequest = try req.content.decode(LoginRequest.self)  // Decode login data
        
        print("email: \(loginRequest.email)")  // Debug print

        // Find the user by email
        guard let user = try await UserModel.query(on: req.db)
            .filter(\.$email == loginRequest.email)
            .first() else {
            return ResultResponse(resultStatusEnum: .unauthorized)
        }

        // Verify the password (assuming it's hashed with Bcrypt)
        guard try user.verify(password: loginRequest.password) else {
            return ResultResponse(resultStatusEnum: .unauthorized)
        }

        // Get user roles and permissions
        let roles = try await user.$roles.query(on: req.db).all()
        let permissions = try await user.$permissions.query(on: req.db).all()

        // Map roles and permissions to their names
        let roleNames = roles.map { $0.name }
        let permissionNames = permissions.map { $0.name }

        // Create a JWT payload with user info, roles, and permissions
        let payload = UserPayload(
            subject: .init(value: user.id!.uuidString),
            roles: roleNames,
            permissions: permissionNames,
            expirationInMinutes: 1440  // Token expiration time (24 hours)
        )

        // Generate the JWT token
        let token = try await req.jwt.sign(payload)

        // Create the AuthUser object with token and user info
        let authUser = AuthUser(
            uid: user.id!,
            username: user.username,
            email: user.email,
            authorization: token,
            avatarUrl: user.profileImage ?? "https://i.pravatar.cc/150?u=a042581f4e29026704d"
        )
        
        return ResultResponse(resultStatusEnum: .success, data: authUser)
    }

    // Read All Users
    // 获取所有用户
    func getAll(req: Request) throws -> EventLoopFuture<ResultResponse<[UserModel]>> {
        return UserModel.query(on: req.db).all().map { users in
            // 将结果封装在 ResultResponse 中
            return ResultResponse(resultStatusEnum: .success, data: users)
        }.flatMapError { error in
            // 处理错误并返回适当的响应
            let response = ResultResponse<[UserModel]>(resultStatusEnum: .badRequest)
            return req.eventLoop.makeSucceededFuture(response)
        }
    }

    // Read a Specific User by ID
    func get(req: Request) throws -> EventLoopFuture<UserModel> {
        UserModel.find(req.parameters.get("userID"), on: req.db)
            .unwrap(or: Abort(.notFound))
    }

    // Update a User
    func update(req: Request) throws -> EventLoopFuture<UserModel> {
        let updatedUser = try req.content.decode(UserModel.self)
        return UserModel.find(req.parameters.get("userID"), on: req.db)
            .unwrap(or: Abort(.notFound))
            .flatMap { user in
                user.username = updatedUser.username
                user.email = updatedUser.email
                user.passwordHash = updatedUser.passwordHash
                return user.update(on: req.db).map { user }
            }
    }

    // Delete a User
    func delete(req: Request) throws -> EventLoopFuture<HTTPStatus> {
        UserModel.find(req.parameters.get("userID"), on: req.db)
            .unwrap(or: Abort(.notFound))
            .flatMap { $0.delete(on: req.db) }
            .transform(to: .noContent)
    }
}
