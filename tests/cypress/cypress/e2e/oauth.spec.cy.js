describe("oauth", () => {
  it("should return oauth token given valid username and password", () => {
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
      .then(resp => {
        expect(resp.status).to.eq(200)
        expect(Object.keys(resp.body)).to.include("access_token");
      })
  })

  it("should return upload and status scopes when valid username and password provided", () => {
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
      .then(resp => {
        expect(resp.status).to.eq(200)
        expect(Object.keys(resp.body)).to.include("scope");
        expect(resp.body["scope"]).to.include("dex:status")
        expect(resp.body["scope"]).to.include("dex:upload")
        expect(resp.body["scope"]).to.not.include("dex:download")
      })
  })

  it("should return 400 when username not provided", () => {
    cy.api({
      url: `${Cypress.env("DEX_URL")}/oauth`,
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: {
        password: Cypress.env("SAMS_PASSWORD")
      },
      failOnStatusCode: false
    })
      .then(resp => {
        expect(resp.status).to.eq(400)
      })
  })

  it("should return 400 when password not provided", () => {
    cy.api({
      url: `${Cypress.env("DEX_URL")}/oauth`,
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: {
        password: Cypress.env("SAMS_USERNAME")
      },
      failOnStatusCode: false
    })
      .then(resp => {
        expect(resp.status).to.eq(400)
      })
  })
})
