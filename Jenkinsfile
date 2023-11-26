pipeline {
    agent any

    environment {
        WORKSPACE_DIR = "build_workspace_${env.BUILD_ID}"
        DOCKER_IMAGE_NAME = 'app' // Replace with your Docker image name
        DOCKER_IMAGE_TAG = "latest"
        EMAIL_RECIPIENT = 'manunited2006@gmail.com'
        // Optional: If you have a registry other than Docker Hub, specify it here
        DOCKER_REGISTRY = '' // Leave this empty for Docker Hub
        // Optional: The ID for your Docker registry credentials stored in Jenkins
        REGISTRY_CREDENTIALS_ID = 'your-registry-credentials-id'
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

        // New stage for building the Docker image
        stage('Build and Push Docker Image') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
                        // Build the Docker image
                        sh "docker build -t -p 8081:8080 ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
                        // Optional: Push the Docker image to a registry
                        if (DOCKER_REGISTRY && REGISTRY_CREDENTIALS_ID) {
                            docker.withRegistry(DOCKER_REGISTRY, REGISTRY_CREDENTIALS_ID) {
                                sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                            }
                        }
                    }
                }
            }
        }

        stage('Run docker compose') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
                        input message: 'Deploy to production?', ok: 'Deploy'
                        sh 'docker-compose down -v --remove-orphans'
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
