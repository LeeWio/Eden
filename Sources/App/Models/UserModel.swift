//
//  UserModel.swift
//
//
//  Created by 李伟 on 2024/10/9.
//

import Fluent
import JWTKit
import Vapor

final class UserModel: Model, Content, @unchecked Sendable {
    // Table name
    static let schema = "users"

    // Primary key (ID)
    @ID(key: .id)
    var id: UUID?

    // Username field
    @Field(key: "username")
    var username: String

    // Email field
    @Field(key: "email")
    var email: String

    // Password hash (hashed in controller or service layer)
    @Field(key: "password_hash")
    var passwordHash: String

    // User bio
    @Field(key: "bio")
    var bio: String?

    // Profile image URL as String
    @Field(key: "profile_image")
    var profileImage: String?

    // Status field using enum
    @Field(key: "status")
    var status: UserStatusEnum

    @Siblings(through: UserRoleModel.self, from: \.$user, to: \.$role)
    var roles: [RoleModel]

    @Siblings(through: UserPermissionModel.self, from: \.$user, to: \.$permission)
    var permissions: [PermissionModel]

    // Created timestamp
    @Timestamp(key: "created_at", on: .create)
    var createdAt: Date?

    // Updated timestamp
    @Timestamp(key: "updated_at", on: .update)
    var updatedAt: Date?

    // Empty initializer for Fluent
    init() {}

    // Custom initializer for creating new users
    init(
        id: UUID? = nil,
        username: String? = nil,
        email: String,
        passwordHash: String,
        bio: String = "",
        profileImage: String = "",
        status: UserStatusEnum = .ACTIVE
    ) {
        self.id = id
        self.username = username ?? email.split(separator: "@").first.map { String($0) } ?? "default_username"
        self.email = email
        self.passwordHash = passwordHash
        self.bio = bio
        self.profileImage = profileImage.isEmpty ? "https://i.pravatar.cc/150?u=a042581f4e29026704d" : profileImage
        self.status = status
    }
}

// Extend User to make it Authenticatable
extension UserModel: ModelAuthenticatable {
    static let usernameKey = \UserModel.$email
    static let passwordHashKey = \UserModel.$passwordHash

    // Password verification logic
    func verify(password: String) throws -> Bool {
        // Using bcrypt to verify password hash
        try Bcrypt.verify(password, created: passwordHash)
    }
}

// JWT payload structure.
struct UserPayload: JWTPayload {
    // Maps the longer Swift property names to the
    // shortened keys used in the JWT payload.
    enum CodingKeys: String, CodingKey {
        case subject = "sub"
        case expiration = "exp"
        case roles
        case permissions
    }

    // The "sub" (subject) claim identifies the principal that is the
    // subject of the JWT.
    var subject: SubjectClaim

    // The "exp" (expiration time) claim identifies the expiration time on
    // or after which the JWT MUST NOT be accepted for processing.
    var expiration: ExpirationClaim

    var roles: [String]

    var permissions: [String]

    init(subject: SubjectClaim, roles: [String], permissions: [String], expirationInMinutes: Int = 60) {
        self.subject = subject
        self.roles = roles
        self.permissions = permissions

        // 设置过期时间
        let expirationDate = Date().addingTimeInterval(TimeInterval(expirationInMinutes * 60))
        expiration = ExpirationClaim(value: expirationDate)
    }

    // Run any additional verification logic beyond
    // signature verification here.
    // Since we have an ExpirationClaim, we will
    // call its verify method.
    func verify(using _: some JWTKit.JWTAlgorithm) async throws {
        try expiration.verifyNotExpired()
    }
}

struct AuthUser: Content {
    var uid: UUID
    var username: String
    var email: String
    var authorization: String
    var avatarUrl: String

    init(
        uid: UUID,
        username: String,
        email: String,
        authorization: String,
        avatarUrl: String? // 使用可选类型 String?
    ) {
        self.uid = uid
        self.username = username
        self.email = email
        self.authorization = authorization
        self.avatarUrl = avatarUrl ?? "https://i.pravatar.cc/150?u=a042581f4e29026704d"
    }
}
