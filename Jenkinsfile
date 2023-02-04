#!/usr/bin/env groovy

def label = "java"
def gradleVersion = "7.6"

pipeline {


    agent {
        label "${label}"
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 1, unit: 'HOURS')
    }
    triggers {
        githubPush()
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh "./gradlew clean build --gradle-version ${gradleVersion}"
            }
        }
    }
}
