@Library('pipe-dream') _
import com.oradian.pipedream.caching.CacheCollection

def files = load "build/project_2/files.groovy"
def common = load "build/common.groovy"

singlenode('slave') {
    stage('Checkout the code') {
        scm_util.clone 'git@github.com:rpetrano/pipedream-demo.git'
    }

    CacheCollection buildCache = common.buildTheBuild()

    cached_build(files.project2Files) { CacheCollection cc ->
        def image = docker.image("project_2:${buildCache.sha1}")
        image.pull()
        image.inside {
            stage('Build') {
                sh 'project_2/build.sh'
            }

            stage('Deploy') {
                commit("project2_build:${cc.sha1}").push()

                nexus.uploadFileToRaw('demo', "project_2/${cc.sha1}/deployable.txt", 'project_2/deployable.txt')
            }
        }
    }
}
