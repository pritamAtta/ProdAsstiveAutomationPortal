
def environment = params.Environment
def browsertype = params.BrowserType
def headless = params.Headless
def build_ok = true

node{

    parameters {
        choice(
                name: 'BrowserType',
                choices: ['chrome','firefox'],
                description: 'Type of Browser'
        )
        choice(
                name: 'Headless',
                choices: ['true','false'],
                description: 'Headless'
        )
        choice(
                name: 'Environment',
                choices: ['QA'],
                description: 'Environment'
        )
    }

    stage('Download Source Code'){

        echo "Staring the Jenkins build for the Assistive Regression Test Suite"

        git credentialsId: '132f2792-d000-4bcf-ba6d-d868b16d3158', url: 'https://github.com/pritamAtta/channelplay-automated-testing-1.git'

    }

    stage('Check Maven and Allure Reporting Status'){

        sh "mvn --version"

    }

    stage('Executing the maven build'){

        try {
            sh 'echo "Jenkins Chrome Version: `google-chrome --version`"'
        } catch (e) {
            echo "Chrome browser version not found."
        }
        sh "mvn dependency:resolve"
        try{
            sh "mvn verify -Denvironment=QA"

        } catch(e) {

            build_ok = false
            echo e.toString()
        }
    }
    stage('Report Generation'){

        script {
            sh '''
                 if [ -D"allure-results" ] ; then
                    echo "Browser=${BrowserType}
                        Branch=${BUILD_TAG}
                        Headless=${Headless}
                        Environment=${Environment}" > target/allure-results/environment.properties
                 else
                    echo "No results for Allure, skipping report"
                 fi
             '''

            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
            ])
        }
    }
    
    stage('send email notification'){
        def tmpRep = env.BUILD_URL+"allure"
        emailext body:  "Build Status :- ${currentBuild.currentResult} \n\n Job Name :- ${env.JOB_NAME} , build :-  ${env.BUILD_NUMBER}\n\n   More info at: ${env.BUILD_URL} \n\n Please find the allure report at ${tmpRep}", 
                subject: "Jenkins Build Status for Job ${env.JOB_NAME}",
                to: "pritamatta12345@gmail.com, taufik@getassistive.com"
    }

    if(build_ok) {
        currentBuild.result = "SUCCESS"
    } else {
        currentBuild.result = "FAILURE"
    }
}


