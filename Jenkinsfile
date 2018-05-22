@Library('pipe-dream') _

singlenode { PR ->
    PR.notifyBuildInProgress()
    sleep(time: 2, unit: 'SECONDS')

    PR.notifyBuildStatus(true)
    PR.commentPR('bok')
}
