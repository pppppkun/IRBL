pipeline {
    agent any
    environment {
        registryUrl= "registry.cn-hangzhou.aliyuncs.com"       //搭建docker私有仓库（Harbor）或者 用DockerHub 又或者用云平台的“容器镜像服务”
        registry_user= "ppppppkun"
        registry_pass= "Li2000chun"
        repo_url="irbl"
        branch="${env.gitlabSourceBranch}"
    }
    options {
        timestamps()    //设置在项目打印日志时带上对应时间
        disableConcurrentBuilds()   //不允许同时执行流水线，被用来防止同时访问共享资源等
//         timeout(time: 10, unit: 'MINUTES')   // 设置流水线运行超过n分钟，Jenkins将中止流水线
//         buildDiscarder(logRotator(numToKeepStr: '20'))   // 表示保留n次构建历史
    }

    stages{
        stage('Print Message') {      //打印信息
            steps {
                echo '打印信息'
                echo "workspace: ${WORKSPACE}"
                echo "registryUrl: ${registryUrl}"
                echo "image_repository: ${registryUrl}"
                echo "${branch}"
           }
        }
        stage('algorithm Build') {
            when {
                environment name: 'branch', value: 'dev'
            }
            agent{
                label 'master'
            }
            steps{
                echo 'Build Algorithm Module'
                sh 'docker login  --username=${registry_user} --password=${registry_pass} ${registryUrl}'
            }
        }
        stage('Maven Build and Test') {
            when {
                environment name: 'branch', value: 'dev'
            }
            agent{
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps{
                echo 'Business Module Test And Build'
                sh 'mvn -pl business clean package jacoco:report -Dmaven.test.failure.ignore=true'
                jacoco()
            }
	    }
        stage('Image Build'){
            when {
                anyOf {
                    environment name: 'branch', value: 'dev'
                    environment name: 'branch', value: 'master'
                }
            }
            agent{
                label 'master'
            }
            steps{
                echo 'Image Build Stage'
                sh "cd business && docker build . -t ${registryUrl}/${repo_url}/irbl-business:${BUILD_ID}"
                sh "cd algorithm && docker build . -t ${registryUrl}/${repo_url}/irbl-algorithm:${BUILD_ID}"
            }
        }
        stage('Image Push'){
            when {
                anyOf {
                    environment name: 'branch', value: 'dev'
                    environment name: 'branch', value: 'master'
                }
            }
            agent{
                label 'master'
            }
            steps{
                echo 'Image Push Stage'
                sh 'docker login  --username=${registry_user} --password=${registry_pass} ${registryUrl}'
                sh "docker push ${registryUrl}/${repo_url}/irbl-business:${BUILD_ID}"
                sh "docker push ${registryUrl}/${repo_url}/irbl-algorithm:${BUILD_ID}"
            }
        }
        stage('deploy'){
            when {
                anyOf {
                    environment name: 'branch', value: 'dev'
                    environment name: 'branch', value: 'master'
                }
            }
            agent{
                label 'irbl'
            }
            steps{
                sh 'docker login  --username=${registry_user} --password=${registry_pass} ${registryUrl}'
                sh 'docker pull ${registryUrl}/${repo_url}/irbl-business:${BUILD_ID}'
                sh 'docker pull ${registryUrl}/${repo_url}/irbl-algorithm:${BUILD_ID}'
//                 sh "if ( docker ps | grep irbl-business ) then (docker container stop irbl-business && docker container rm irbl-business) fi"
//                 sh "if (ps -ef| grep python | grep irbl-algorithm) then (docker container stop irbl-algorithm && docker container rm irbl-algorithm) fi"
//                 sh "if ( docker ps | grep irbl-algorithm ) then (docker container stop irbl-algorithm && docker container rm irbl-algorithm) fi"
                sh 'sed -i "s#0.0.0#${BUILD_ID}#g" docker/dev/docker-compose.yml'
                sh 'cd docker && cd dev && docker-compose stop && docker-compose up'
            }
        }
    }
}
