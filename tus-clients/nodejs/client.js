const c = {};

c.login = async (username, password, url) => {
  const params = new URLSearchParams({
    username: username,
    password: password,
  });

  return fetch(`${url}/oauth`, {
    method: 'POST',
    body: params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
  .then((response) => {
    if (response.status == 200 && response.statusText === "OK") {
      return response.json();
    }
    console.error(
      `Client login failed to SAMS, error code is ${response.status}, error message is ${response.statusText}`
    );
    return null;
  });
};

module.exports = c;
