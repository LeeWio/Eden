//
//  PermissionModel.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Fluent
import Vapor

final class PermissionModel: Model, Content, @unchecked Sendable {
    static let schema = "permissions"

    @ID(key: .id)
    var id: UUID?

    @Field(key: "name")
    var name: String

    @Field(key: "description")
    var description: String

    @Siblings(through: UserPermissionModel.self, from: \.$permission, to: \.$user)
    var users: [UserModel]

    @Siblings(through: RolePermissionModel.self, from: \.$permission, to: \.$role)
    var roles: [RoleModel]

    init() {}

    init(id: UUID? = nil, name: String) {
        self.id = id
        self.name = name
    }
}
