pipeline {
    agent any

    environment {
        WORKSPACE_DIR = "build_workspace_${env.BUILD_ID}"
        EMAIL_RECIPIENT = 'manunited2006@gmail.com'
        DOCKER_IMAGE_NAME = 'app' // Replace with your actual app name
        DOCKER_IMAGE_TAG = 'latest' // Replace with your actual tag if needed
    }

     stages {
             stage('Prepare Workspace') {
                 steps {
                     sh 'mkdir -p ${WORKSPACE_DIR}'
                 }
             }

             stage('Checkout') {
                 steps {
                     sh 'git clone https://github.com/DonLofto/DevOpsProject.git ${WORKSPACE_DIR}'
                 }
             }

             // New stage for building the WAR file
             stage('Build WAR') {
                 steps {
                     dir("${WORKSPACE_DIR}") {
                         sh 'mvn clean package' // This assumes you have a Maven project
                     }
                 }
             }



        stage('Build Docker Image') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
                        // Build the Docker image
                        sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
                    }
                }
            }
        }

        stage('Run docker compose') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
                        input message: 'Deploy to production?', ok: 'Deploy'
                        echo "Bringing down any existing Docker containers"
                        sh 'docker-compose down -v --remove-orphans'
                        echo "Starting Docker containers"
                        sh 'docker-compose up -d'
                    }
                }
            }
        }

    }

    post {
        success {
            echo 'Deployment successful!'
            emailext(
                subject: 'Jenkins Notification - Deployment Successful',
                body: 'Deployment of your application was successful.',
                to: "${EMAIL_RECIPIENT}"
            )
            script {
                sh 'docker image prune -f'
            }
        }
        always {
            sh "rm -rf ${WORKSPACE_DIR}"
        }
    }
}
