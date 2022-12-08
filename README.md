## Spring RSocket/RSocket-java: server file descriptors exhaustion with idle connections

### DESCRIPTION

RSocket/RSocket-java ([1.1.1](https://github.com/rsocket/rsocket-java/releases/tag/1.1.1) and below) integration 
from spring-boot (2.6.6 and below) is affected server file descriptors exhaustion with idle connections.

Malicious client may open TCP connection, but do not proceed with RSocket protocol handshake -
sending [SETUP](https://github.com/rsocket/rsocket/blob/master/Protocol.md#setup-frame-0x01) frame. 
RSocket/RSocket-java does not close such idle connections which may remain open for a long time.

Malicious clients may open arbitrary number of idle connections that exhaust server's available file descriptors, 
make It unable to accept further connections.

Eventually fixed in RSocket/Rocket-java [1.1.2](https://github.com/rsocket/rsocket-java/pull/1027). 

Initially reported on springframework [issue](https://github.com/spring-projects/spring-framework/issues/27373) tracker
whose members are in charge of the project.

### PREREQUISITES

jdk 8+

### SETUP

Spring-boot 2.6.6 based application having `RSocket-java` service (`springboot-rsocket-service` module).

Malicious RSocket client (small subset of protocol, sufficient for vulnerability demonstration) is implemented with Netty 
(`dos-idle-connections-client` module).

It establishes 20_000 TCP connections, but does not proceed with SETUP frame, or any traffic once connection is established. 

### RUNNING

Build server, client binaries `./gradlew clean build installDist`

Run server `./springboot_rsocket_service.sh` 

Run client `./overflow_client.sh` 

Client opens 20_000 idle connections which are never closed by server.

```
14:37:59.045 [pool-1-thread-1] INFO example.client.Main - connections, opened: 13000, closed: 0, rejected: 0
14:38:04.045 [pool-1-thread-1] INFO example.client.Main - connections, opened: 18000, closed: 0, rejected: 0
14:38:09.045 [pool-1-thread-1] INFO example.client.Main - connections, opened: 20000, closed: 0, rejected: 0
14:38:14.045 [pool-1-thread-1] INFO example.client.Main - connections, opened: 20000, closed: 0, rejected: 0
14:38:19.045 [pool-1-thread-1] INFO example.client.Main - connections, opened: 20000, closed: 0, rejected: 0
```
