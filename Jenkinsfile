pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
            post {
                failure {
                    mail (
                        to: "luisenriquem15@gmail.com",
                        subject: "Fallo en Etapa SCM",
                        body: "La etapa SCM ha fallado."
                    )
                }
            }
        }
        stage('Unit Tests') {
            steps {
                dir('api') {
                    sh "./gradlew test"
                }
            }
            post {
                failure {
                    mail (
                        to: "luisenriquem15@gmail.com",
                        subject: "Fallo en Etapa Unit Tests",
                        body: "La etapa Unit Tests ha fallado."
                    )
                }
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
            post {
                failure {
                    mail (
                        to: "luisenriquem15@gmail.com",
                        subject: "Fallo en Etapa SonarQube Analysis",
                        body: "La etapa SonarQube Analysis ha fallado."
                    )
                }
            }
        }
    }
}
