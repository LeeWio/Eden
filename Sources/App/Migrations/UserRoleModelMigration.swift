//
//  UserRoleModelMigration.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Fluent
import Vapor

struct UserRoleModelMigration: Migration {
    func prepare(on database: Database) -> EventLoopFuture<Void> {
        database.schema(UserRoleModel.schema)
            .id()
            .field("user_id", .uuid, .required, .references("users", "id", onDelete: .cascade))
            .field("role_id", .uuid, .required, .references("roles", "id", onDelete: .cascade))
            .create()
    }

    func revert(on database: Database) -> EventLoopFuture<Void> {
        database.schema(UserRoleModel.schema).delete()
    }
}
