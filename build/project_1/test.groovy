@Library('pipe-dream') _
import com.oradian.ci.caching.CacheCollection;

def files = load "build/project_1/files.groovy"

singlenode('slave') {
    stage('Checkout the code') {
        scm_util.clone 'git@github.com:rpetrano/pipedream-demo.git'
    }

    def sha1 = files.sha1
    cached_build("project1_tests", sha1) {
        def image = docker.image("project1_build:${sha1}")
        image.inside {
            stage('Run tests') {
                sh "sleep 18 && exit 0"
            }
        }
    }
}
