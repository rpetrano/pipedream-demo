project1 = load "build/project_1/files.groovy"
project2 = load "build/project_2/files.groovy"

def getSmoketestFiles() {
    return project1.project1Files + project2.project2Files + [ 'build/smoketest' ]
}

def getSha1() { return cached_build.getSha1(getSmoketestFiles()) }

return this
