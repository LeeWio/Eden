//
//  File.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Vapor
import Fluent

final class RolePermissionModel: Model,@unchecked Sendable {
    static let schema = "role_permission"
    
    @ID(key: .id)
    var id: UUID?

    @Parent(key: "role_id")
    var role: RoleModel

    @Parent(key: "permission_id")
    var permission: PermissionModel
    
    init() {}

    init(id: UUID? = nil, roleID: UUID, permissionID: UUID) {
        self.id = id
        self.$role.id = roleID
        self.$permission.id = permissionID
    }
    
}
