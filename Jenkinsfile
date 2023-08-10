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
                        to: "luisenriquem15@gmail.com",
                        subject: "Fallo en Etapa SonarQube BackEnd",
                        body: "La etapa SonarQube Analysis para el backend ha fallado."
                    )
                }
            }
        }

        // stage("SonarQube Back Quality Gate") {
        //     steps {
        //         timeout(time: 5, unit: 'MINUTES') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }

        //     post {
        //         failure {
        //             mail (
        //                 to: "luisenriquem15@gmail.com",
        //                 subject: "Fallo en Control de Calidad SonarQube BackEnd",
        //                 body: "El análisis de SonarQube para el backend no superó el nivel de calidad esperado",
        //             )
        //         }
        //     }
        // }

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
                        to: "luisenriquem15@gmail.com",
                        subject: "Fallo en Etapa SonarQube Analysis FrontEnd",
                        body: "La etapa SonarQube Analysis para el frontend ha fallado."
                    )
                }
            }
        }

        // stage("SonarQube Front Quality Gate") {
        //     steps {
        //         timeout(time: 5, unit: 'MINUTES') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }

        //     post {
        //         failure {
        //             mail (
        //                 to: "luisenriquem15@gmail.com",
        //                 subject: "Fallo en Control de Calidad SonarQube FrontEnd",
        //                 body: "El análisis de SonarQube para el frontend no superó el nivel de calidad esperado",
        //             )
        //         }
          
        //     }
        // }
    }
}