//
//  UserPermissionModelMigration.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Fluent
import Vapor

struct UserPermissionModelMigration: Migration {
    func prepare(on database: Database) -> EventLoopFuture<Void> {
        database.schema(UserPermissionModel.schema)
            .id()
            .field("user_id", .uuid, .required, .references("users", "id", onDelete: .cascade))
            .field("permission_id", .uuid, .required, .references("permissions", "id", onDelete: .cascade))
            .create()
    }

    func revert(on database: Database) -> EventLoopFuture<Void> {
        database.schema(UserPermissionModel.schema).delete()
    }
}
