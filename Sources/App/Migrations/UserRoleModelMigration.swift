//
//  File.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Vapor
import Fluent

struct UserRoleModelMigration: Migration {
    func prepare(on database: Database) -> EventLoopFuture<Void> {
        return database.schema(UserRoleModel.schema)
            .id()
            .field("user_id", .uuid, .required, .references("users", "id", onDelete: .cascade))
            .field("role_id", .uuid, .required, .references("roles", "id", onDelete: .cascade))
            .create()
    }

    func revert(on database: Database) -> EventLoopFuture<Void> {
        return database.schema(UserRoleModel.schema).delete()
    }
}
