pipeline {
    agent any
    stages {
//         stage('Hello') {
//             steps {
//                 echo "Hello world"
//                     }
//             }
        
        step('SonarQube') {
            node {
                stage('SCM') {
                    checkout scm
                }
                stage('SonarQube Analysis') {
                    withSonarQubeEnv() {
                    sh "./gradlew sonar"
                    }
                }
            }
        }

    // }
    // post{
    //     always{
    //         mail to: "luisenriquem15@gmail.com",
    //         subject: "Sonar Test",
    //         body: "Este es un test de Sonar xddd"
    //     }
    }
}