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
})
