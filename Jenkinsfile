pipeline {
    agent any

    environment {
        WORKSPACE_DIR = "build_workspace_${env.BUILD_ID}" // The workspace directory for the build.
        DOCKER_IMAGE_TAG = "latest" // The Docker image tag to be used.
    }

    stages {
        stage('Prepare Workspace') {
            steps {
                cleanWs()
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

        stage('Deploy') {
            steps {
                script {
                    // Navigate to the dynamic workspace directory
                    dir("${WORKSPACE_DIR}") {
                        // Stop the current db-app service if it's running
                        sh 'docker-compose stop db-app || true'
                        // Remove the current db-app service container without removing the volume
                        sh 'docker-compose rm -f db-app || true'
                        // Find containers using port 9090 and stop them
                        sh "docker ps --filter 'status=running' --format '{{.Ports}} {{.ID}}' | grep ':9090' | awk '{print $NF}' | xargs -r docker stop || true"

                        // After stopping, you can remove the containers as well
                        sh "docker ps -a --filter 'status=exited' --format '{{.Ports}} {{.ID}}' | grep ':9090' | awk '{print $NF}' | xargs -r docker rm || true"
                        sh 'docker-compose rm -f back || true'

                        // Start the db-app and back services
                        sh 'docker-compose up -d'
                    }
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