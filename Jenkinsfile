pipeline {
  agent any

  stages {
    stage('compile') {
      steps {
        sh 'mvn clean install'
      }
    }
    stage('archive') {
      steps {
        parallel(
          "Junit": {
            junit 'target/surefire-reports/*.xml'

          },
          "Archive": {
            archiveArtifacts(artifacts: 'target/galaxycore*.jar', onlyIfSuccessful: true, fingerprint: true)
            archiveArtifacts(artifacts: 'target/*javadoc.jar', fingerprint: true)

          }
        )
      }
    }
  }
}