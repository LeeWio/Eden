//
//  UserPermissionModel.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Fluent
import Vapor

final class UserPermissionModel: Model, @unchecked Sendable {
    static let schema = "user_permission"

    @ID(key: .id)
    var id: UUID?

    @Parent(key: "user_id")
    var user: UserModel

    @Parent(key: "permission_id")
    var permission: PermissionModel

    init() {}

    init(id: UUID? = nil, userID: UUID, permissionID: UUID) {
        self.id = id
        $user.id = userID
        $permission.id = permissionID
    }
}
