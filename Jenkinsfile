pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                echo "Hello world"
                    }
            }
        }
    post{
        always{
            mail to: "luisenriquem15@gmail.com",
            subject: "Test Email",
            body: "Test"
        }
    }
}