// the first start with pipeline { set up agent and stages inside pipeline }
pipeline {

    // any คือ ใช้ executor ใด ๆ ก็ได้
    agent any
    environment {
            // you have to call tru env.<var name> ex, env.DOMAIN
            JAR_TARGET = 'target/basic-api-using-spring-boot-3-0.0.1-SNAPSHOT.jar'
            CONTAINER_NAME = 'basic-api'
            CONTAINER_RUNNER_NAME = 'backend'
            CONTAINER_VERSION = 'latest'
            CONTAINER_PORT_OUT = '8080'
            CONTAINER_PORT_IN = '8080'
    }

    // stages as working Flows tell Pipeline what gonna do
    stages {
            stage('Before initial check software installed') {
                steps {
                      sh 'java -version'
                      sh 'mvn -version'
                      sh 'git --version'
                      sh 'docker --version'
                }
            }

            stage('Checkout git repo') {
                steps {
                    // Checks out the source code from your Git repository. *** Note, by default it will pull repo to C:\ProgramData\Jenkins\.jenkins\workspace\...
                    git branch: 'ttknp-spring-boot-3', url: 'https://github.com/Thitikorn-Nupan/jenskins-and-spring-boot-crud-array-list-collection.git'
                }
            }


            stage('Test maven') {
                steps {
                    // Compiles and runs the project's unit tests using the Surefire plugin (for unit tests) and Failsafe plugin (for integration tests).
                    sh "mvn clean test"
                }
                post {
                      success {
                          echo 'After test java successfully.'
                      }
                      failure { // If got error from stage this condition is working
                          echo 'After test java failed.'
                      }
                }
            }


            stage('Build maven') {
                steps {
                    // Shows current working directory (e.g., /var/jenkins_home/workspace/my-pipeline)
                    sh 'pwd'

                    // Go to target dir
                    dir('target') {
                        echo 'Before build jar'
                        sh "ls -l"
                    }

                    // Builds the Spring Boot application using maven
                    sh "mvn clean install -DskipTests"

                    // Returns to the original working directory
                    sh 'pwd'
                    // Go to target dir
                    dir('target') {
                        echo 'After build jar'
                        sh "ls -l"
                    }
                    // Returns to the original working directory
                    sh 'pwd'
                }
            }

            stage('Build docker') {
                steps {
                    sh "docker build -t ${env.CONTAINER_NAME}:${env.CONTAINER_VERSION} --build-arg JAR_FILE=${env.JAR_TARGET} ."

                }
                post {
                     success {
                         echo 'After build successfully.'
                         sh 'docker images' // check is image create
                     }
                }
            }

            stage('Deploy') {
                steps {
                    sh "docker run --name ${env.CONTAINER_RUNNER_NAME} -p ${env.CONTAINER_PORT_OUT}:${env.CONTAINER_PORT_IN} -d ${env.CONTAINER_NAME}:${env.CONTAINER_VERSION}"
                }
                post {
                      success {
                          echo 'After run successfully.'
                          sh 'docker ps' // check is image running
                      }
                }
            }

        }
        // The post section can be defined at both the global Pipeline level and within individual stage blocks, allowing for granular control over post-execution actions.
        post {
                  /*
                    always: Steps within this block execute regardless of the Pipeline's or stage's final status (success, failure, unstable, aborted).
                    success: Steps execute only if the Pipeline or stage completes successfully.
                    failure: Steps execute only if the Pipeline or stage fails.
                    unstable: Steps execute only if the Pipeline or stage completes with an "unstable" status.
                    aborted: Steps execute only if the Pipeline or stage is aborted.
                    changed: Steps execute if the current run's status differs from the previous run's status.
                    fixed: Steps execute if the current run is successful and the previous run was either failed or unstable.
                    regression: Steps execute if the current run's status is worse than the previous run's status (e.g., successful to unstable, unstable to failure).
                    cleanup: This is a special condition within the global post section, primarily used for tasks like workspace cleanup, regardless of the build result.
                 */
                 always {
                     echo 'Pipeline deploy spring boot + docker finished.'
                 }
                 success { // If some it is failure success won't work
                     echo 'Pipeline deploy spring boot + docker completed successfully.'
                     sh 'pwd'
                 }
                 failure { // After failure on stages alert this still alert too (last process)
                     echo 'Pipeline deploy spring boot + docker failed.'
                 }
        }
    }
