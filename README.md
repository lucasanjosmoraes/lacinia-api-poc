# lacinia-api-poc

POC a GraphQL API using [Lacinia](https://github.com/walmartlabs/lacinia). It follows the tutorial from the [Lacinia Docs](https://lacinia.readthedocs.io/en/latest/tutorial/index.html).

## Usage

## Before start

You will need to execute some scripts to run your application properly. They are available in the `bin` directory and help
you to create a database using Docker, define the tables required by the application and also add some data for testing purpose.

### From REPL

There are commands available to manage the **API** using **REPL**:
```clj
;; To start the API
(start)

;; To stop
(stop)

;; You can use this helper to execute queries or mutations
(q "{ game_by_id(id: \"1234\") { name designers { name games { name }}}}")
```

### From Intellij

There are scripts in the directory `.run` that allow you to run REPL and all the tests available. You just need to choose 
one of them from the dropdown menu and press play 🛀🏽.

## License

Copyright © 2022 Lucas dos Anjos Moraes

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
