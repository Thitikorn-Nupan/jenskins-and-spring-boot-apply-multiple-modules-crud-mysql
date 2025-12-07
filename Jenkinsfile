// the first start with pipeline { set up agent and stages inside pipeline }
pipeline {
    // any คือ ใช้ executor ใด ๆ ก็ได้
    agent any
    environment {
            // you have to call tru env.<var name> ex, env.DOMAIN
            JAR_TARGET = 'target/api-using-crud-mysql-0.0.1-SNAPSHOT.jar'
            DOCKER_APP_PORT_REMOTE = '6789'
            DOCKER_DB_USERNAME = 'ttknp'
            DOCKER_DB_PASSWORD = '12345'
            DOCKER_DB_NAME = 'to_do_apps_docker'
            DOCKER_DB_PORT_REMOTE = '3307'
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
                    git branch: 'ttknp', url: 'https://github.com/Thitikorn-Nupan/jenskins-and-spring-boot-apply-multiple-modules-crud-mysql.git'
                }
            }



            stage('Build docker container database') {
                steps {
                   sh "docker build -t mysql:latest --build-arg USERNAME=${env.DOCKER_DB_USERNAME} --build-arg PASSWORD=${env.DOCKER_DB_PASSWORD} --build-arg DATABASE=${env.DOCKER_DB_NAME} .  -f dockerfiles/database/Dockerfile"
                }
                post {
                     success {
                         echo 'After build successfully.'
                         sh 'docker images' // check is image create
                     }
                }
            }



           stage('Deploy docker image database') {
                steps {
                   sh "docker run -d --name=mysql_database -p ${env.DOCKER_DB_PORT_REMOTE}:3306 -e MYSQL_ROOT_PASSWORD=${env.DOCKER_DB_PASSWORD} -it mysql:latest"
                }
                post {
                     success {
                         echo 'After run successfully.'
                         sh 'docker ps' // check is container running create
                     }
                }
            }

            stage('Before build maven') {
                steps {
                    sh "mvn clean compile -DskipTests"
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
                    // Returns to the original working directory
                    sh 'pwd'
                    // Builds the Spring Boot application using maven

                    sh "mvn clean install -DskipTests"
                    // Returns to the original working directory
                    // Go to target dir
                    dir('target') {
                        echo 'After build jar'
                        sh "ls -l"
                    }
                    // Returns to the original working directory
                    sh 'pwd'
                }
            }



            stage('Build docker container app') {
                steps {
                    sh "docker build -t springboot:latest --build-arg JAR_FILE=${env.JAR_TARGET} --build-arg JDBC_USERNAME=${env.DOCKER_DB_USERNAME} --build-arg JDBC_PASSWORD=${env.DOCKER_DB_PASSWORD} --build-arg JDBC_DATABASE=${env.DOCKER_DB_NAME} . -f dockerfiles/app/Dockerfile"
                }
                post {
                     success {
                         echo 'After build successfully.'
                         sh 'docker images' // check is image create
                     }
                }
            }



            stage('Deploy docker image app') {
                steps {
                    sh "docker run --name springboot_app -p ${env.DOCKER_APP_PORT_REMOTE}:6789 -d springboot:latest"
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
                 }
                 failure { // After failure on stages alert this still alert too (last process)
                     echo 'Pipeline deploy spring boot + docker failed.'
                 }
        }
    }
