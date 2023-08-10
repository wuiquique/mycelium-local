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
                always {
                    script {
                        def response = sh(script: 'curl -s -u admin:admin http://sonarqube:9000/api/issues/search?componentKeys=mycelium:api -d "severities=BLOCKER,CRITICAL,MAJOR" -d "statuses=OPEN" -d "types=CODE_SMELL"', returnStdout: true).trim()
                        if (response.contains("total\":0")) {
                            echo "No hay deuda técnica"
                        } else {
                            mail (
                                to: "correo_especifico@example.com",
                                subject: "Deuda Técnica Detectada en SonarQube",
                                body: "Se ha detectado deuda técnica en el proyecto."
                            )
                        }
                    }
                }
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
