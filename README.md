* `js-console` fará o papel do ODIN

* `app-profile-jee-html5` fará o papel do HORIZON (openstack)

* `rest` responsável por criar o cookie no *Keycloak*

* `config/realm-export.json` realm + client do *Keycloak* que poderá ser importado.

  

## Start Keycloak

```shell
./bin/standalone.sh -b 0.0.0.0 -Djboss.socket.binding.port-offset=100
```



## Deploy it

> Change to it project folder and execute de command bellow:

```shell
mvn clean package wildfly:deploy -Denforcer.skip=true -Dmaven.test.skip=true -Dwildfly.port=10090 -Dorg.slf4j.simpleLogger.defaultLogLevel=WARN
```



## Test It

* http://localhost:8180/js-console/
* http://localhost:8180/app-profile-html5/