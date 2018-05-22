@Library('pipe-dream') _
import com.oradian.ci.caching.CacheCollection

def files = load "build/project_1/files.groovy"
def common = load "build/common.groovy"

singlenode('slave') {
    stage('Checkout the code') {
        scm_util.clone 'git@github.com:rpetrano/pipedream-demo.git'
    }

    CacheCollection buildCache = common.buildTheBuild()

    cached_build(files.project1Files) { CacheCollection cc ->
        def image = docker.image("project_1:${buildCache.sha1}")
        image.pull()
        image.inside {
            stage('Build') {
                sh 'project_1/build.sh'
            }

            stage('Deploy') {
                commit("project1_build:${cc.sha1}").push()

                nexus.uploadFileToRaw('demo', "project_1/${cc.sha1}/deployable.txt", 'project_1/deployable.txt')
            }
        }
    }
}
