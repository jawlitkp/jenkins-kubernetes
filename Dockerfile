FROM jenkins/jenkins:2.60.3-alpine

RUN /usr/local/bin/install-plugins.sh \
    workflow-aggregator \
    docker-workflow:1.12 \
    workflow-job:2.12.2 \
    kubernetes:0.12 \
    blueocean:1.2.0 \
    workflow-durable-task-step \
    script-security \
    ansicolor \
    log-parser \
    git \
    ansible \
    http_request \
    cucumber-testresult-plugin \
    job-dsl \
    authorize-project \
    copyartifact \
    role-strategy

ENV JAVA_OPTS="-Dorg.apache.commons.jelly.tags.fmt.timeZone=Asia/Taipei -Djenkins.install.runSetupWizard=false"

COPY init.groovy.d /usr/share/jenkins/ref/init.groovy.d
ADD jenkins.CLI.xml $JENKINS_HOME
ADD javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration.xml $JENKINS_HOME

USER root
RUN apk --no-cache add sudo
COPY sudoers.d /etc/sudoers.d
COPY entrypoint.sh /entrypoint.sh
# COPY plugins  /usr/share/jenkins/ref/plugins

RUN chown -R jenkins.jenkins "$JENKINS_HOME" /usr/share/jenkins/ref

USER jenkins
ENTRYPOINT ["/bin/tini","--","/entrypoint.sh"]
