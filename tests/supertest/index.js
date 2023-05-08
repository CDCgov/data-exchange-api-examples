const request = require('supertest');

describe("root", () => {
  it("should response with 200", async () => {
    await request(process.env.DEX_URL)
      .get("/")
      .expect(200)
  })
})

