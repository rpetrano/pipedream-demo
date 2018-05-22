@Library('pipe-dream') _
import com.oradian.pipedream.caching.CacheCollection

def files = load "build/smoketest/files.groovy"
def common = load "build/common.groovy"

singlenode('slave') {
    stage('Checkout the code') {
        scm_util.clone 'git@github.com:rpetrano/pipedream-demo.git'
    }

    CacheCollection buildCache = common.buildTheBuild()

    cached_build('smoketest', files.sha1) {
        def image = docker.image("smoketest:${common.sha1}")
        image.pull()
        image.inside {
            stage('Run smoke tests') {
                sh 'sleep 19s'
            }
        }
    }
}
