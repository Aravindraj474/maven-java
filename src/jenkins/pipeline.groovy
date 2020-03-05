#!groovy

pipeline {

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', daysToKeepStr: '7'))
    }

    agent {
        label 'master'
    }

    parameters {
        string(name: 'gitBranch', defaultValue: 'master', description: 'example : feature/search')
        string(name: 'tagName', defaultValue: 'stage', description: 'tag to execute')
        choice(
                name: 'Profiles',
                choices: "ddc4c\ncdc4c\ncdc7\ncdc2c\ncdc8c\nddc1\nddc1c\nddc3c\nddc4\ncdc7c\ndefault\nlocal\npdc1c\nstagecert\nstageprod\nstagedev",
                description: 'Environment to execute against'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                retry(3) {
                    checkout scm: [
                            $class             : 'GitSCM'
                            , userRemoteConfigs: [[
                                                          url            : 'https://github.com/Aravindraj474/maven-java'
                                                          , credentialsId: ''
                                                  ]]
                            , branches         : [[name: "${params.gitBranch}"]]
                    ], poll: false
                }
            }
        }

        stage('Execute Maven') {
            steps {
                cmd "mvn clean test -Dprofile=${params.Profiles} \"-Dcucumber.options=--tags '@test'\" -DGIT_BRANCH=${params.gitBranch}"
            }
            post {
                always {
                    step([$class: 'CucumberReportPublisher', jsonReportDirectory: "./target", jenkinsBasePath: '', fileIncludePattern: 'cucumber.json'])
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/rest-logs/**', fingerprint: true
            archiveArtifacts artifacts: 'target/cucumber-report-html/**', fingerprint: true
        }
    }

}