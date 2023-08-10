pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }
        stage('UnitTest') {
            steps {
                dir('api') {
                    sh "./gradlew test"
                }
            }
        }
        stage('SonarQube') {
            steps {
                script {
                    withSonarQubeEnv('Mycelium-dev') {
                        dir('api') {
                            sh "./gradlew sonar"
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
