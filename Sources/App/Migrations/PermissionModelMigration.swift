//
//  File.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Vapor
import Fluent

struct PermissionModelMigration: Migration {
    func prepare(on database: any Database) -> EventLoopFuture<Void> {
        database.schema(PermissionModel.schema)
            .id()
            .field("name", .string, .required)
            .field("description", .string, .required)
            .create()
    }
    
    func revert(on database: Database) -> EventLoopFuture<Void> {
        database.schema(PermissionModel.schema).delete()
    }
}
