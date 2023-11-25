/**
 * This Jenkinsfile defines a pipeline for a CI/CD process.
 * The pipeline is divided into several stages, each performing a specific task.
 */
pipeline {
    // The agent directive tells Jenkins to allocate an executor for the pipeline.
    agent any

    // The environment directive is used to define environment variables that will be accessible within the pipeline.
    environment {
        WORKSPACE_DIR = "build_workspace_${env.BUILD_ID}" // The workspace directory for the build.
        DOCKER_IMAGE_TAG = "latest" // The Docker image tag to be used.
        EMAIL_RECIPIENT = 'manunited2006@gmail.com' // The recipient of the email notifications.
    }

    // The stages directive contains all the stages that will be executed in the pipeline.
    stages {
        // This stage prepares the workspace for the build.
        stage('Prepare Workspace') {
            steps {
                // The sh step executes a shell command.
                sh 'mkdir -p ${WORKSPACE_DIR}'
            }
        }

        // This stage checks out the code from the Git repository.
        stage('Checkout') {
            steps {
                sh 'git clone https://github.com/DonLofto/DevOpsProject.git ${WORKSPACE_DIR}'
            }
        }

        // This stage runs Docker Compose to manage the application's services.
        stage('Run docker compose') {
            steps {
                dir("${WORKSPACE_DIR}") {
                    script {
                        // The input step pauses pipeline execution and allows the user to interact and control the flow of the build.
                        input message: 'Deploy to production?', ok: 'Deploy'
                        sh 'docker-compose down -v --remove-orphans'
                        sh 'docker-compose up -d'
                    }
                }
            }
        }
    }

    // The post section runs after the pipeline's run and is usually used for clean up and notification tasks.
    post {
        // The success condition runs if the pipeline completed successfully.
        success {
            echo 'Deployment successful!'
            // The emailext step sends an email depending on the status of the pipeline.
            emailext(
                subject: 'Jenkins Notification - Deployment Successful',
                body: 'Deployment of your application was successful.',
                to: "${EMAIL_RECIPIENT}"
            )
            script {
                sh 'docker image prune -f'
            }
        }
        // The always condition runs regardless of the completion status of the pipeline.
        always {
            sh "rm -rf ${WORKSPACE_DIR}"
        }
    }
}