//
//  File.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Vapor
import Fluent

final class UserRoleModel: Model,@unchecked Sendable {
    static let schema = "user_role"
    
    @ID(key: .id)
    var id: UUID?

    @Parent(key: "user_id")
    var user: UserModel

    @Parent(key: "role_id")
    var role: RoleModel

    init() {}

    init(id: UUID? = nil, userID: UUID, roleID: UUID) {
        self.id = id
        self.$user.id = userID
        self.$role.id = roleID
    }
    
}
