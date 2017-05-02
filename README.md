# Environment

This is supposed to be deployed on EAP 6 or 7. JDG libaries will be included in WEB-INF/lib instead of using JBoss Modules.


# How to build

    mvn clean package

It assumes the Red Hat Maven repository, https://maven.repository.redhat.com/ga/, is configured in your `~/.m2/settings.xml`.


# Configuration

To run 2 node cluster on a single machine, edit domain.xml and host.xml respectively.

domain.xml
~~~
    <server-groups>
        <server-group name="main-server-group" profile="default">
            <jvm name="default">
                <heap size="1000m" max-size="1000m"/>
                <permgen max-size="256m"/>
            </jvm>
            <socket-binding-group ref="standard-sockets"/>
        </server-group>
    </server-groups>
~~~

host.xml
~~~
    <servers>
        <server name="server-one" group="main-server-group">
            <system-properties>
                <property name="jgroups.bind_addr" value="127.0.0.1"/>
                <property name="jgroups.tcp.port" value="7800"/>
                <property name="jgroups.tcp.initial_hosts" value="127.0.0.1[7800],127.0.0.1[7900]"/>
            </system-properties>
        </server>
        <server name="server-two" group="main-server-group" auto-start="false">
            <socket-bindings port-offset="100"/>
            <system-properties>
                <property name="jgroups.bind_addr" value="127.0.0.1"/>
                <property name="jgroups.tcp.port" value="7900"/>
                <property name="jgroups.tcp.initial_hosts" value="127.0.0.1[7800],127.0.0.1[7900]"/>
            </system-properties>
        </server>
    </servers>
~~~

Then start the domain by domain.sh and deploy the war file.
Note that server-two is not auto-start to ensure server-one to be the coordinator.

~~~
$ $EAP_HOME/bin/domain.sh &
$ $EAP_HOME/bin/jboss-cli.sh
[domain@localhost:9999 /] deploy /path/to/jdg-query-test/target/jdg-query-test.war --all-server-groups
[domain@localhost:9999 /] /host=master/server-config=server-two:start
(Test using curl like in the next section.)
[domain@localhost:9999 /] undeploy jdg-query-test.war --all-relevant-server-groups
[domain@localhost:9999 /] /host=master/server-config=server-two:stop
[domain@localhost:9999 /] shutdown --host=master
[disconnected /]
$ 
~~~


# Test

The cache automatically starts on deployment and insert one record in the cache in ``SampleContextListener.java``.

    curl "http://127.0.0.1:8080/jdg-query-test/jdg"  ==> Call mass indexer
    curl "http://127.0.0.1:8080/jdg-query-test/jdg2" ==> Call Hibernate Search (Lucene Query)
    curl "http://127.0.0.1:8080/jdg-query-test/jdg3" ==> Call Infinispan Query DSL


# To open in Eclipse

    mvn eclipse:eclipse

Then import into your workspace.
