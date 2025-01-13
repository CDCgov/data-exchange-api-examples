# Public Health Data Operations (PHDO) API Examples

## Overview

This repository contains example upload client scripts demonstrating use PHDO upload services. <br/>

### PHDO Upload API Environment Hostnames
| Environment   | Hostname               |
| ----          | ---------------------- |
| Development   | https://apidev.cdc.gov |
| Test          | https://apitst.cdc.gov |
| Staging       | https://apistg.cdc.gov |
| Production    | https://api.cdc.gov    |

## Setup

### Authentication via Secure Authentication Management Services (SAMS)
Users must first authenticate with SAMS before using the Upload API and retrieve an authorization token via a POST request to the **/auth** API endpoint.

The OAuth request must have the following header: 
- `Content-Type: application/x-www-form-urlencoded`

The following query parameters must be provided:
- `username` - SAMS system account username
- `password` - SAMS system account password

Example request: `https://<hostname>/oauth?username=<your-username>&password=<your-password>`

### Provide Environment Variables via Dotenv
Once the hostname has been selected and token retrieved, those values must be set to environment variables for the example scripts to use. These can be set at the command line, or using `dotenv`. To do this, create a file in the root of this repo called `.env`. <br/>

Add the following lines to the .env file and fill out their values:
```
AUTH_TOKEN=
DEX_URL=
```

## Disclaimer
The client examples provided in this repository are meant to serve as a starting point in the development of a client and are not intended to be a final solution or a recommendation for implementation. The individual requirements of an entity implementing a client is strictly the responsibility of that entity. <br>

Examples provided include clients written in nodejs, python, java, and javascript (for use in a browser), serving as a subset of potential implementation options. Please note that the metadata fields provided in the clients are subject to change as PHDO upload services evolve and may not be kept current with PHDO code and the metadata values provided in the example clients are strictly placeholder values.

## Public Domain Standard Notice
This repository constitutes a work of the United States Government and is not subject to domestic copyright protection under 17 USC § 105. This repository is in the public domain within the United States, and copyright and related rights in the work worldwide are waived through the [CC0 1.0 Universal public domain dedication](https://creativecommons.org/publicdomain/zero/1.0/). All contributions to this repository will be released under the CC0 dedication. By submitting a pull request you are agreeing to comply with this waiver of copyright interest.

## License Standard Notice
The repository utilizes code licensed under the terms of the Apache Software
License and therefore is licensed under ASL v2 or later. <br/>

This source code in this repository is free: you can redistribute it and/or modify it under the terms of the Apache Software License version 2, or (at your option) any later version. <br/>

This source code in this repository is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Apache Software License for more details. <br/>

You should have received a copy of the Apache Software License along with this program. If not, see http://www.apache.org/licenses/LICENSE-2.0.html. <br/>

The source code forked from other open source projects will inherit its license.

## Privacy Standard Notice
This repository contains only non-sensitive, publicly available data and information. All material and community participation is covered by the [Disclaimer](https://github.com/CDCgov/template/blob/master/DISCLAIMER.md) and [Code of Conduct](https://github.com/CDCgov/template/blob/master/code-of-conduct.md). For more information about CDC's privacy policy, please visit [http://www.cdc.gov/other/privacy.html](https://www.cdc.gov/other/privacy.html).

## Contributing Standard Notice
Anyone is encouraged to contribute to the repository by [forking](https://help.github.com/articles/fork-a-repo) and submitting a pull request. (If you are new to GitHub, you might start with a [basic tutorial](https://help.github.com/articles/set-up-git).) By contributing to this project, you grant a world-wide, royalty-free, perpetual, irrevocable, non-exclusive, transferable license to all users under the terms of the [Apache Software License v2](http://www.apache.org/licenses/LICENSE-2.0.html) or later. <br/>

All comments, messages, pull requests, and other submissions received through CDC including this GitHub page may be subject to applicable federal law, including but not limited to the Federal Records Act, and may be archived. Learn more at [http://www.cdc.gov/other/privacy.html](http://www.cdc.gov/other/privacy.html).

## Records Management Standard Notice
This repository is not a source of government records, but is a copy to increase collaboration and collaborative potential. All government records will be published through the [CDC web site](http://www.cdc.gov).

## Additional Standard Notices
Please refer to [CDC's Template Repository](https://github.com/CDCgov/template) for more information about [contributing to this repository](https://github.com/CDCgov/template/blob/master/CONTRIBUTING.md), [public domain notices and disclaimers](https://github.com/CDCgov/template/blob/master/DISCLAIMER.md), and [code of conduct](https://github.com/CDCgov/template/blob/master/code-of-conduct.md).
