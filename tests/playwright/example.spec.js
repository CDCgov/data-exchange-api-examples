import { test, expect } from '@playwright/test';

test("GET", async ({ request, baseURL }) => {
 const _response = await request.get(baseURL);
 expect(_response.ok()).toBeTruthy();
 expect(_response.status()).toBe(200);
 console.log(await _response.status());
});

test("GET_FAIL", async ({ request, baseURL }) => {
 const _response = await request.get(baseURL + "/api/junk/path");
 expect(_response.ok()).toBeTruthy();
 expect(_response.status()).toBe(200);
 console.log(await _response.status());
});