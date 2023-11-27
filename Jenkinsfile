pipeline {
    agent any

    environment {
        WORKSPACE_DIR = "build_workspace_${env.BUILD_ID}" // The workspace directory for the build.
        DOCKER_IMAGE_TAG = "latest" // The Docker image tag to be used.
    }

    stages {
        stage('Prepare Workspace') {
            steps {
                // The sh step executes a shell command.
                sh 'mkdir -p ${WORKSPACE_DIR}'
            }
        }
        stage('Cleanup') {
            steps {
                script {
                    // Check if the application and database containers are running
                    // before pruning unused networks.
                    def appRunning = sh(script: "docker-compose ps | grep back", returnStatus: true) == 0
                    def dbRunning = sh(script: "docker-compose ps | grep db-app", returnStatus: true) == 0

                    if (appRunning && dbRunning) {
                        // Prune unused networks with caution
                        sh 'docker network prune -f'
                    } else {
                        echo "Skipping network pruning because services are not running as expected."
                    }
                }
            }
        }

        stage('Checkout') {
            steps {
                // The dir step allows you to change the current working directory.
                sh 'git clone https://github.com/DonLofto/DevOpsProject.git ${WORKSPACE_DIR}'
            }
        }

        stage('Confirm Deployment') {
            steps {
                script {
                    input message: 'Deploy to production?', ok: 'Deploy'
                }
            }
        }

        stage('Run docker compose') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    // Stop and remove only the application container
                    sh 'docker-compose stop back'
                    sh 'docker-compose rm -f back'

                    // Rebuild and start only the application container
                    sh 'docker-compose build back'
                    sh 'docker-compose up -d back'
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }
    }
}