def getProject2Files() {
    return [
        'build/project_2',
        'project_2'
    ]
}

def getSha1() { return cached_build.getSha1(getProject2Files()) }

return this
