pipeline {
    agent any

    environment {
        // Common environment variables
        WORKSPACE_DIR = "build_workspace_${env.BUILD_ID}"
        EMAIL_RECIPIENT = 'manunited2006@gmail.com'
        DOCKER_IMAGE_NAME = 'app'
        DOCKER_IMAGE_TAG = 'latest'

        // MySQL environment variables
        MYSQL_IMAGE = 'mysql:8.0'
        MYSQL_CONTAINER_NAME = 'db-app'
        MYSQL_DATABASE = 'PetitionAppDB'
        MYSQL_ROOT_PASSWORD = 'secret'
        MYSQL_USER = 'app'
        MYSQL_PASSWORD = 'root'

        // Application environment variables
        SPRING_DATASOURCE_URL = "jdbc:mysql://${MYSQL_CONTAINER_NAME}:3306/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
        SPRING_DATASOURCE_USERNAME = '${MYSQL_USER}'
        SPRING_DATASOURCE_PASSWORD = '${MYSQL_PASSWORD}'
    }

    stages {
        stage('Prepare Workspace') {
            steps {
                sh 'mkdir -p ${WORKSPACE_DIR}'
            }
        }

        stage('Checkout') {
            steps {
                sh 'git clone https://github.com/DonLofto/DevOpsProject.git'
            }
        }

        stage('Build WAR') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
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
                // Clean up unused Docker images
                sh 'docker image prune -f || true'
            }
        }
        always {
            // Clean up the workspace without affecting the running application
            sh "rm -rf ${WORKSPACE_DIR}"
        }
    }
}
