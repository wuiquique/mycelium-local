pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube Server Name') {
                        sh "./gradlew sonar"
                    }
                }
            }
        }
    }
    post {
        always {
            emailext (
                to: "luisenriquem15@gmail.com",
                subject: "Sonar Test",
                body: "Este es un test de Sonar xddd"
            )
        }
    }
}
