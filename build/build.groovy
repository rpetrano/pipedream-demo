@Library('pipe-dream') _
common = load "build/common.groovy"

def build_image(String name, boolean cache = true) {
    def branch = scm_util.branch
    def sha1 = common.sha1

    stage("Docker build $name") {
        dir("build/$name") {
            def uid = sh(script: "id -u", returnStdout: true).trim()
            def gid = sh(script: "id -g", returnStdout: true).trim()

            def args = "--build-arg=BUILD_UID=$uid --build-arg=BUILD_GID=$gid ."
            if (!cache)
                args = "--no-cache $args"

            sh "sed -e 's/FROM build_base\$/FROM build_base:$sha1/' -i Dockerfile"

            def tags = [ sha1, scm_util.branch ]
            if (scm_util.branch == "master")
                tags += "latest"

            def image = docker.build("$name:$sha1", args)
            tags.each{image.push(it)}
        }
    }
}

dream_build {
    singlenode('dockerbuilder', true) {
        stage('Checkout the code') {
            scm_util.clone('git@github.com:rpetrano/pipedream-demo.git')
        }

        parallel(
            'BuildDockerProject1' : { build_image('project_1') },
            'BuildDockerProject2' : { build_image('project_2') },
            'BuildDockerSmoketest' : { build_image('smoketest') }
        )
    }
}
