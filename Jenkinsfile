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
                    withSonarQubeEnv('Mycelium-dev') {
                        dir('api') {
                            sh "./gradlew sonar"
                        }
                    }
                }
            }
        }
        stage('Unit Tests') {
            steps {
                dir('api') {
                    sh "./gradlew test"
                }
            }
        }
    }
    post {
        always {
            script {
                currentBuild.resultIsBetterOrEqualTo('FAILURE')  // Check if build result is FAILURE or WORSE
                if (currentBuild.resultIsBetterOrEqualTo('FAILURE')) {
                    mail (
                        to: "luisenriquem15@gmail.com",
                        subject: "Fallo en Pipeline",
                        body: "El pipeline ha fallado en la etapa de ${currentBuild.currentStage.name}"
                    )
                }
            }
        }
    }
}
