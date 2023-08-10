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
                        dir('api') {
                            sh "../gradlew sonar"
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            mail (
                to: "luisenriquem15@gmail.com",
                subject: "Sonar Test",
                body: "Este es un test de Sonar xddd"
            )
        }
    }
}
