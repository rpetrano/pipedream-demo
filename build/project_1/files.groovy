def getProject1Files() {
    return [
        'build/project_1',
        'project_1'
    ]
}

def getSha1() { return cached_build.getSha1(getProject1Files()) }

return this
