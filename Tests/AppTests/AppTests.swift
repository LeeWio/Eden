@testable import App
import Fluent
import XCTVapor

final class AppTests: XCTestCase {
    var app: Application!

    func testHelloWorld() async throws {
        try await app.test(.GET, "hello", afterResponse: { res async in
            XCTAssertEqual(res.status, .ok)
            XCTAssertEqual(res.body.string, "Hello, world!")
        })
    }
}
