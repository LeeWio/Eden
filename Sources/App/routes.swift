import Fluent
import Vapor

// Define the routes for the application
func routes(_ app: Application) throws {
    try app.register(collection: UserController())
}
