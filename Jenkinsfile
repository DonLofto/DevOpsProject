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

        stage('Checkout') {
            steps {
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
                    sh 'docker-compose down -v --remove-orphans'
                    sh 'docker-compose up -d'
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
