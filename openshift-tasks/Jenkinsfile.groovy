// This is a Jenkinsfile written in Groovy. We did not cover
// much of Groovy in class - but if you feel more comfortable you can use this file.
// Rename the other Jenkinsfile to something else (like Jenkinsfile.declarative) and rename
// this one to 'Jenkinsfile' before you push to your repo. The build config will look for
// just 'Jenkinsfile'
// Implement the sections marked with "TBD:"

#!groovy
podTemplate(
  label: "skopeo-pod",
  cloud: "openshift",
  inheritFrom: "maven",
  containers: [
    containerTemplate(
      name: "jnlp",
      image: "image-registry.openshift-image-registry.svc:5000/${GUID}-jenkins/jenkins-agent-appdev",
      resourceRequestMemory: "1Gi",
      resourceLimitMemory: "2Gi",
      resourceRequestCpu: "1",
      resourceLimitCpu: "2"
    )
  ]
) {
  node('skopeo-pod') {
    // Define Maven Command to point to the correct
    // settings for our Nexus installation
    def mvnCmd = "mvn -s ./nexus_settings.xml"

    // Define global variables
    def imageName = "${GUID}-tasks"
    def devProject = "${GUID}-tasks-dev"
    def prodProject = "${GUID}-tasks-prod"

    // Checkout Source Code.
    stage('Checkout Source') {
      checkout scm

      // Patch Source artifactId to include GUID
      sh "sed -i 's/GUID/${GUID}/g' openshift-tasks/pom.xml"
    }

    // Build the Tasks Application in the directory 'openshift-tasks'
    dir('openshift-tasks') {
      // Extract version from the pom.xml
      def version = getVersionFromPom("pom.xml")

      // TBD Set the tag for the development image: version + build number
      def devTag  = ""
      // Set the tag for the production image: version
      def prodTag = ""

      // Using Maven build the war file
      // Do not run tests in this step
      stage('Build war') {
        echo "Building version ${devTag}"

        // TBD: Execute Maven Build

      }

      // Using Maven run the unit tests
      stage('Unit Tests') {
        echo "Running Unit Tests"

        // TBD: Execute Unit Tests

      }

      // Using Maven to call SonarQube for Code Analysis
      stage('Code Analysis') {
        echo "Running Code Analysis"

        // TBD: Execute Sonarqube Tests
        //      Your project name should be "${GUID}-${JOB_BASE_NAME}-${devTag}"

      }

      // Publish the built war file to Nexus
      stage('Publish to Nexus') {
        echo "Publish to Nexus"

        // TBD: Publish to Nexus

      }

      // Build the OpenShift Image in OpenShift and tag it.
      stage('Build and Tag OpenShift Image') {
        echo "Building OpenShift container image ${imageName}:${devTag}"

        // TBD: Build Image, tag Image
        //      Make sure the image name is correct!

      }

      // Deploy the built image to the Development Environment.
      stage('Deploy to Dev') {
        echo "Deploying container image to Development Project"

        // TBD: Deploy to development Project
        //      Set Image, Set VERSION
        //      (Re-) Create ConfigMap
        //      Make sure the application is running and ready before proceeding

      }

      // Copy Image to Nexus container registry
      stage('Copy Image to Nexus container registry') {
        echo "Copy image to Nexus container registry"

        // TBD: Copy image to Nexus container registry

        // TBD: Tag the built image with the production tag.

      }

      // Blue/Green Deployment into Production
      // -------------------------------------
      def destApp   = "tasks-green"
      def activeApp = ""

      stage('Blue/Green Production Deployment') {
        // TBD: Determine which application is active
        //      Set Image, Set VERSION
        //      (Re-)create ConfigMap
        //      Deploy into the other application
        //      Make sure the application is running and ready before proceeding

      }

      stage('Switch over to new Version') {
        echo "Switching Production application to ${destApp}."
        // TBD: Execute switch
        //      Hint: sleep 5 seconds after the switch for the route to be fully
        //            switched over

      }
    }
  }
}

// Convenience Functions to read version from the pom.xml
// Do not change anything below this line.
// --------------------------------------------------------
def getVersionFromPom(pom) {
  def matcher = readFile(pom) =~ '<version>(.+)</version>'
  matcher ? matcher[0][1] : null
}