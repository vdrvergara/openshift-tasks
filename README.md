# Openshift-tasks
Describe openshift Tasks in Jenkin for OCP 4.3

* Tasks Demo Application

This is an example JEE application to be used in the OpenShift 4 Advanced Application Deployment.

  * Prepare your environment

1. Create projects  
~~~    
  $ bin/setup_projects.sh demo <user> true
~~~


2. Setup Jenkin
    
~~~
  $ bin/setup_jenkins.sh demo <git_repository> <ocp4 console url>
~~~

3. Configure development project
    
~~~    
  $ bin/setup_dev.sh demo
~~~

Make sure to read the README in the `openshift-tasks` folder for details on how to configure the tasks application.
