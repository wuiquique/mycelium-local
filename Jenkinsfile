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
                        to: "jflores@unis.edu.gt",
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
                        to: "jflores@unis.edu.gt",
                        subject: "Fallo en Etapa Unit Tests",
                        body: "La etapa Unit Tests ha fallado."
                    )
                }
            }
        }

        stage('SonarQube Back') {
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
                        to: "jflores@unis.edu.gt",
                        subject: "Fallo en Etapa SonarQube BackEnd",
                        body: "La etapa SonarQube Analysis para el backend ha fallado."
                    )
                }
            }
        }

        stage("SonarQube Back Quality Gate") {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }

            post {
                failure {
                    mail (
                        to: "jflores@unis.edu.gt",
                        subject: "Fallo en Control de Calidad SonarQube BackEnd",
                        body: "El análisis de SonarQube para el backend no superó el nivel de calidad esperado",
                    )
                }
            }
        }

        stage('SonarQube Front') {
            steps {
                script {
                    nodejs('Mycelium-front') {
                        def scannerHome = tool 'SonarScanner';
                        withSonarQubeEnv() {
                            dir('client'){
                                sh "${scannerHome}/bin/sonar-scanner"
                            }
                        }
                    }
                }
            }
            post {
                failure {
                    mail (
                        to: "jflores@unis.edu.gt",
                        subject: "Fallo en Etapa SonarQube Analysis FrontEnd",
                        body: "La etapa SonarQube Analysis para el frontend ha fallado."
                    )
                }
            }
        }

        stage("SonarQube Front Quality Gate") {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }

            post {
                failure {
                    mail (
                        to: "jflores@unis.edu.gt",
                        subject: "Fallo en Control de Calidad SonarQube FrontEnd",
                        body: "El análisis de SonarQube para el frontend no superó el nivel de calidad esperado",
                    )
                }
          
            }
        }

        stage("Build Back") {
            steps {
                script {
                    sh "podman build -t local-registry:5000/mycelium-local_api:dev -f Dockerfile.prod ./api"
                }
            }

            post {
                failure {
                    mail (
                        to: "jflores@unis.edu.gt",
                        subject: "Falló de build del Docker Back",
                        body: "Build de Docker para el Back fallo",
                    )
                }
            }
        }

        stage("Build Front") {
            steps {
                script {
                    sh "podman build -t local-registry:5000/mycelium-local_client:dev -f Dockerfile.prod ./client"
                }
            }

            post {
                failure {
                    mail (
                        to: "jflores@unis.edu.gt",
                        subject: "Falló de build del Docker Front",
                        body: "Build de Docker para el Front fallo",
                    )
                }
            }
        }

        stage("Publish images") {
            steps {
                script {
                    sh "podman push local-registry:5000/mycelium-local_api:dev"
                    sh "podman push local-registry:5000/mycelium-local_client:dev"
                }
            }

            post {
                failure {
                    mail (
                        to: "jflores@unis.edu.gt",
                        subject: "Imágenes no publicadas",
                        body: "No se pudieron publicar las imágenes al local-registry",
                    )
                }
            }
        }

        stage("Deployment") {
            steps {
                sshPublisher(
                    failOnError: true,
                    publishers: [
                        sshPublisherDesc(
                            configName: "Dev", 
                            transfers: [
                                sshTransfer(
                                    execCommand: 'docker compose pull && docker compose up -d',
                                    execTimeout: 3600000
                                )
                            ]
                        )
                    ]
                )
            }

            post {
                failure {
                    mail (
                        to: "jflores@unis.edu.gt",
                        subject: "Los contenedores no se pudieron ejecutar",
                        body: "No se pudo ejecutar los contenedores actualizados en las computadoras",
                    )
                }
            }
        }

    }
}