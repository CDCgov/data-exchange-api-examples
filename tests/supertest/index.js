const request = require('supertest');

describe("root", () => {
  it("should response with 200", async () => {
    await request("https://apidev.cdc.gov")
      .get("/")
      .expect(200)
  })
})

