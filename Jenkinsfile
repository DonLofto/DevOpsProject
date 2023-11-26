pipeline {
    agent any

    environment {
        // Common environment variables
        WORKSPACE_DIR = "build_workspace_${env.BUILD_ID}"
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
                dir("${WORKSPACE_DIR}") {
                    sh 'git clone https://github.com/DonLofto/DevOpsProject.git .'
                }
            }
        }


        stage('Build WAR') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    sh 'mvn clean package'
                }
            }
        }
        stage('Approve Deployment') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
                        input message: 'Deploy to production?', ok: 'Deploy'
                    }
                }
            }
        }

        stage('Create Network') {
            steps {
                script {
                    // Create a custom Docker network
                    sh 'docker network create you-app-network'
                }
            }
        }

        stage('Start MySQL') {
            steps {
                script {
                    // Start MySQL container
                    sh 'docker pull ${MYSQL_IMAGE}'
                    sh """
                    docker run --name ${MYSQL_CONTAINER_NAME} -d --network you-app-network \
                    --restart always \
                    -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                    -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                    -e MYSQL_USER=${MYSQL_USER} \
                    -e MYSQL_PASSWORD=${MYSQL_PASSWORD} \
                    -p 3306:3306 \
                    -v \$(pwd)/db:/docker-entrypoint-initdb.d \
                    ${MYSQL_IMAGE}
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
                        sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
                        sh "docker run -d --name ${DOCKER_IMAGE_NAME} --network you-app-network \
                        -p 9090:8080 ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                    }
                }
            }
        }


    }

   post {
       success {
           echo 'Deployment successful!'
           archiveArtifacts allowEmptyArchive: true, artifacts: '**/*.war'
       }
       always {
           // Clean up the workspace without affecting the running application
           sh "rm -rf ${WORKSPACE_DIR}"
       }
   }
}
