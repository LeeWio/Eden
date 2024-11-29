//
//  File 2.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Fluent

struct RolePermissionModelMigration: Migration {
    func prepare(on database: Database) -> EventLoopFuture<Void> {
        return database.schema(RolePermissionModel.schema)
            .id()
            .field("role_id", .uuid, .required, .references("roles", "id", onDelete: .cascade))
            .field("permission_id", .uuid, .required, .references("permissions", "id", onDelete: .cascade))
            .create()
    }

    func revert(on database: Database) -> EventLoopFuture<Void> {
        return database.schema(RolePermissionModel.schema).delete()
    }
}
