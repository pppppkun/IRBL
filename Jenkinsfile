pipeline {
    agent any
    environment {
        registryUrl= "swr.cn-north-4.myhuaweicloud.com"       //搭建docker私有仓库（Harbor）或者 用DockerHub 又或者用云平台的“容器镜像服务”
        registry_user= "cn-north-4@YRBK3MFZ6D5CBRR0ALEK"
        registry_pass= "62fc1c853cd95cb8e1de855ab492bbb43106f62696b9c9ab3b25dc34b8855d55"
//         docker login -u cn-north-4@YRBK3MFZ6D5CBRR0ALEK -p 62fc1c853cd95cb8e1de855ab492bbb43106f62696b9c9ab3b25dc34b8855d55 swr.cn-north-4.myhuaweicloud.com
    }
    options {
        timestamps()    //设置在项目打印日志时带上对应时间
        disableConcurrentBuilds()   //不允许同时执行流水线，被用来防止同时访问共享资源等
        timeout(time: 10, unit: 'MINUTES')   // 设置流水线运行超过n分钟，Jenkins将中止流水线
//         buildDiscarder(logRotator(numToKeepStr: '20'))   // 表示保留n次构建历史
    }

    stages{
        stage('Print Message') {      //打印信息
            steps {
                echo '打印信息'
                echo "workspace: ${WORKSPACE}"
                echo "registryUrl: ${registryUrl}"
                echo "image_repository: ${registryUrl}"
           }
        }
        stage('Maven Build and Test') {
            agent{
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps{
                echo 'Business Module Test And Build'
                sh 'mvn -pl business -Ptest test'
                sh 'mvn -pl business package -Dmaven.test.skip=true'
            }
	    }
        stage('Image Build'){
            agent{
                label 'master'
            }
            steps{
                echo 'Image Build Stage'
                sh "cd business && docker build . -t ${registryUrl}/irbl-business:${BUILD_ID}"
            }
        }
        stage('Image Push'){
            agent{
                label 'master'
            }
            steps{
                echo 'Image Push Stage'
                sh 'docker login  --username=${registry_user} --password=${registry_pass} ${registryUrl}'
                sh "docker push ${registryUrl}/irbl-business:${BUILD_ID}"
            }
        }
        stage('deploy'){
            agent{
                label 'irbl'
            }
            steps{
                sh 'docker login  --username=${registry_user} --password=${registry_pass} ${registryUrl}'
                sh 'docker pull ${registryUrl}/irbl-business:${BUILD_ID}'
                sh "if (ps -ef| grep java|grep irbl-business) then (docker container stop irbl-business && docker container rm irbl-business) fi"
                sh "docker run -p 8080:8080 --name irbl-business -v /log:/log -d ${registryUrl}/irbl-business:${BUILD_ID}"
            }
        }
    }
}