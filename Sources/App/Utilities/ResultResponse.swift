//
//  ResultResponse.swift
//
//
//  Created by 李伟 on 2024/10/27.
//

import Vapor

struct ResultResponse<T: Content>: Content, @unchecked Sendable {
    let status: Int
    let message: String
    let data: T?

    init(resultStatusEnum: ResultStatusEnum, data: T? = nil) {
        status = resultStatusEnum.rawValue
        message = resultStatusEnum.description
        self.data = data
    }
}
