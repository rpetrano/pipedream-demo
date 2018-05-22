@Library('pipe-dream') _

import com.oradian.pipedream.integration.Github

dream_build {
    def BU = new BuildUtil(this)
    def PR = Github.create(this)

    def success = false

    try {
        PR.notifyBuildInProgress()
        BU.abortPreviousBuilds()

        stage('Build the build') {
            singlenode('dockerbuilder') {
                scm_util.clone 'git@github.com:rpetrano/pipedream-demo.git'

                def common = load "build/common.groovy"
                common.buildTheBuild()
            }
        }

        def parameters = [ string(name: 'gitBranch', value: scm_util.branch) ]
        def project1Build = { build(job: 'Project1-Build', parameters: parameters) }
        def project2Build = { build(job: 'Project2-Build', parameters: parameters) }
        def project1Test  = { build(job: 'Project1-Test',  parameters: parameters) }
        def project2Test  = { build(job: 'Project2-Test',  parameters: parameters) }
        def smokeTests    = { build(job: 'Smoketest',      parameters: parameters) }
        def status1 = null
        def status2 = null

        def runSmokeTests = {
            if (status1 == 'SUCCESS' && status2 == 'SUCCESS')
                smokeTests()
        }

        stage('Build') {
            parallel(
                "Project1": {
                    status1 = project1Build().result
                    parallel(
                        "smoke": runSmokeTests,
                        "Project1Test": { project1Test() }
                    )
                },
                "Project2": {
                    status2 = project2Build().result
                    parallel(
                        "smoke": runSmokeTests,
                        "Project2Test": { project2Test() }
                    )
                }
            )
        }

        if (scm_util.branch == 'master') {
            stage('Deploy') {
                build(job: 'Deploy')
            }
        }

        success = true
    } finally {
        PR.notifyBuildStatus(success)
    }
}
