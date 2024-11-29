//
//  File.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Vapor
import Fluent

final class RoleModel: Model,Content,@unchecked Sendable {
    static let schema = "roles"
    
    @ID(key: .id)
    var id: UUID?
    
    @Field(key: "name")
    var name: String
    
    @Field(key: "description")
    var description: String
    
    @Siblings(through: UserRoleModel.self, from: \.$role, to: \.$user)
    var users: [UserModel]
    
    @Siblings(through: RolePermissionModel.self, from: \.$role, to: \.$permission)
    var permissions: [PermissionModel]
}
