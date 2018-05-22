@Library('pipe-dream') _
import com.oradian.pipedream.caching.CacheCollection;

def files = load "build/project_2/files.groovy"

singlenode('slave') {
    stage('Checkout the code') {
        scm_util.clone 'git@github.com:rpetrano/pipedream-demo.git'
    }

    def sha1 = files.sha1
    cached_build("project2_tests", sha1) {
        def image = docker.image("project2_build:${sha1}")
        image.inside {
            stage('Run tests') {
                sh "sleep 18 && exit 0"
            }
        }
    }
}
