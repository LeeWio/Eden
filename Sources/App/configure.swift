import Fluent
import FluentMySQLDriver
import JWT
import NIOSSL
import Vapor

// configures your application
public func configure(_ app: Application) async throws {
    // uncomment to serve files from /Public folder
    // app.middleware.use(FileMiddleware(publicDirectory: app.directory.publicDirectory))
    var tls = TLSConfiguration.makeClientConfiguration()
    tls.certificateVerification = .none

    app.databases.use(.mysql(
        hostname: "127.0.0.1",
        port: MySQLConfiguration.ianaPortNumber,
        username: "root",
        password: "Lw001208...",
        database: "blog",
        tlsConfiguration: tls
    ), as: .mysql)

    // 添加迁移
    app.migrations.add(UserModelMigration())
    app.migrations.add(RoleModelMigration())
    app.migrations.add(PermissionModelMigration())
    app.migrations.add(UserPermissionModelMigration())
    app.migrations.add(UserRoleModelMigration())
    app.migrations.add(RolePermissionModelMigration())
    app.migrations.add(PostModelMigration())

    app.middleware.use(JwtAuthenticationMiddleware())

//     Add HMAC with SHA-256 signer
    await app.jwt.keys.add(hmac: "SHA-256", digestAlgorithm: .sha256)

    // 配置 CORS 中间件
    let corsConfig = CORSMiddleware.Configuration(
        allowedOrigin: .custom("http://127.0.0.1:3000"), // 允许的前端来源
        allowedMethods: [.GET, .POST, .PUT, .DELETE, .OPTIONS, .PATCH], // 允许的 HTTP 方法
        allowedHeaders: [.accept, .authorization, .contentType, .origin, .xRequestedWith]
    )
    let corsMiddleware = CORSMiddleware(configuration: corsConfig)
    app.middleware.use(corsMiddleware) // 添加 CORS 中间件

    do {
        try await app.autoMigrate()
        app.logger.info("Migration completed successfully.")
    } catch {
        app.logger.error("Migration failed: \(error.localizedDescription)")
    }

    app.logger.logLevel = .debug

    // register routes
    try routes(app)
}
