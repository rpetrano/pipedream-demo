def getBuildFiles() {
    return [ 'build' ]
}

def buildTheBuild() {
    def result

    stage('Build the build') {
        def buildFiles = getBuildFiles()
        result = cached_build(buildFiles) {
            load 'build/build.groovy'
        }
    }

    return result
}

def getSha1() { return cached_build.getSha1(getBuildFiles()) }

return this
