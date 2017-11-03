node {
    stage('Prep') {
        deleteDir()
        git url: 'https://github.com/mattbrasieratos/ol_dummy_mediator'
        
        // set BUILD_TIMESTAMP
        def now = new Date()
        env.BUILD_TIMESTAMP = now.format("yyyyMMdd-HHmmss", TimeZone.getTimeZone('UTC'))
        echo "${BUILD_TIMESTAMP}"
    
        // capture GID of Docker group
        env.DOCKER_GID = sh (
            script: 'ls -la /var/run/docker.sock|cut -d" " -f4',
            returnStdout: true
        ).trim()
        echo "Docker GID: ${DOCKER_GID}"
    }
    // Maven build steps
    withDockerContainer(image: 'maven:3-jdk-8',
          args: '''--network="citools"
                   -v /var/run/docker.sock:/var/run/docker.sock
                   --group-add ${DOCKER_GID}''') {

        stage('Build') {
          sh 'mvn -B compile'
        }
        
        stage('Dependency Check') {
          sh 'mvn -B org.owasp:dependency-check-maven:2.1.0:check'
        }


        stage('Package') {
          sh 'mvn -B package'
        }


        stage('Containerise') {
          sh "git rev-parse HEAD > build.id"
          sh 'mvn -B docker:build'
        }
    }
    withDockerContainer(image: 'maven:3-jdk-8',
      args: '''
               -v /var/run/docker.sock:/var/run/docker.sock
                --group-add ${DOCKER_GID}''') { 
    	 stage('Integration Test') {
          sh 'mvn -P test -B test'
        }
         stage('Unit Test') {
//         sh 'mvn clean package docker:build'
//         sh 'mvn -P unit-test -B test'
        }

    }
    withDockerContainer(image: 'maven:3-jdk-8',
          args: '''--network="citools"
                   -v /var/run/docker.sock:/var/run/docker.sock
                   --group-add ${DOCKER_GID}''') {
        stage('Sonar Check') {
          withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'sonar',
                        usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASS']]) {
            sh '''mvn -B sonar:sonar \
                -Dsonar.host.url=http://sonar:9000 \
                -Dsonar.login=${SONAR_USER} \
                -Dsonar.password=${SONAR_PASS}'''
          }
        }
    }
    
    stage('Publish WAR') {
        withCredentials([usernameColonPassword(credentialsId: 'nexus', variable: 'USERPASS')]) {
            sh '''curl -v -u ${USERPASS} --upload-file target/dummy-mediation.war \
                     http://nexus:8081/repository/maven-snapshots/net/atos/dummy-mediation/${BUILD_TIMESTAMP}-SNAPSHOT/dummy-mediation-${BUILD_TIMESTAMP}-SNAPSHOT.war'''
        }
    }

    stage('Publish Image') {
        def img = docker.image('dummy-mediation:0.0.1-SNAPSHOT');

        docker.withRegistry('http://nexus:80', 'nexus') {
        sh "git rev-parse HEAD > .git/commit-id"
        def commit_id = readFile('.git/commit-id').trim()

        println commit_id
        img.push();
        img.push('latest')
	}
    }
}
