//
//  File.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Fluent

struct PostModelMigration: Migration {
    func prepare(on database: Database) -> EventLoopFuture<Void> {
        database.schema(PostModel.schema)
            .id()
            .field("title", .string, .required)
            .field("content", .string, .required)
            .field("authorId", .uuid, .required)
            .field("createdAt", .datetime)
            .field("updatedAt", .datetime)
            .create()
    }

    func revert(on database: Database) -> EventLoopFuture<Void> {
        database.schema(PostModel.schema).delete()
    }
}
