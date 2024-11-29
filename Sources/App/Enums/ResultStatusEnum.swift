//
//  File.swift
//  
//
//  Created by 李伟 on 2024/10/27.
//

import Foundation



// ResultEnum 提供了 API 响应状态码和描述
enum ResultStatusEnum: Int, CustomStringConvertible {
    case success = 200
    case badRequest = 400
    case userNotFound = 404
    case unauthorized = 401  // 补充未授权状态码
    case serverError = 500  // 服务器错误状态码（可以加上）


    var description: String {
        switch self {
        case .success:
            return ResultConstants.SUCCESS
        case .badRequest:
            return ResultConstants.BADREQUEST
        case .userNotFound:
            return ResultConstants.USER_NOT_FOUND
        case .unauthorized:
            return ResultConstants.UNAUTHORIZED
        case .serverError:
            return ResultConstants.SERVER_ERROR
        }
    }
}
