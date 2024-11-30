//
//  File.swift
//  Eden
//
//  Created by lucas on 11/29/24.
//

import Vapor
import Fluent

final class PostModel: Model,Content,@unchecked Sendable {
    static let schema = "posts"
    
    @ID(key: .id)
    var id: UUID?
    
    @Field(key: "title")
    var title: String
    
    @Field(key: "content")
    var content: String
    
    @Field(key: "authorId")
    var authorId: UUID
    
    @Timestamp(key: "createdAt", on: .create)
    var createdAt: Date?
    
    @Timestamp(key: "updatedAt", on: .update)
    var updatedAt: Date?
    
    init() {}

    init(id: UUID? = nil, title: String, content: String, authorId: UUID) {
        self.id = id
        self.title = title
        self.content = content
        self.authorId = authorId
    }
}
