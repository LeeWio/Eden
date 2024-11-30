//
//  RoleModelMigration.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Fluent

struct RoleModelMigration: Migration {
    func prepare(on database: any Database) -> EventLoopFuture<Void> {
        database.schema(RoleModel.schema)
            .id()
            .field("name", .string, .required)
            .field("description", .string, .required)
            .create()
    }

    func revert(on database: Database) -> EventLoopFuture<Void> {
        database.schema(RoleModel.schema).delete()
    }
}
