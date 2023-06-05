describe("upload", () => {
  it("should return tus welcome message when valid auth token provided", async () => {
    const accessToken = await new Promise((res, rej) => {
      cy.api({
        url: `${Cypress.env("DEX_URL")}/oauth`,
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: {
          username: Cypress.env("SAMS_USERNAME"),
          password: Cypress.env("SAMS_PASSWORD")
        }
      })
        .then(resp => res(resp.body["access_token"]))
    })

    expect(accessToken).to.not.be.undefined;

    cy.api({
      url: `${Cypress.env("DEX_URL")}/upload`,
      method: "GET",
      headers: {
        "Authorization": `Bearer ${accessToken}`
      }
    })
      .then(resp => {
        expect(resp.status).to.eq(200)
        expect(resp.body).to.include("Welcome to tusd")
      })
  })

  it("should return unauthorized when no auth token provided", () => {
    cy.api({
      url: `${Cypress.env("DEX_URL")}/upload`,
      method: "GET",
      failOnStatusCode: false
    })
      .then(resp => {
        expect(resp.status).to.eq(401)
      })
  })

  it("should return unauthorized when invalid auth token provided", () => {
    cy.api({
      url: `${Cypress.env("DEX_URL")}/upload`,
      method: "GET",
      headers: {
        "Authorization": "Bearer 12345"
      },
      failOnStatusCode: false
    })
      .then(resp => {
        expect(resp.status).to.eq(401)
      })
  })
})
