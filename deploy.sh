#bin/sh

app=$1

if [ $app == "infra" ]
then
  mvn clean package -f infrastructure/pom.xml
  cd infrastructure
  cdk deploy InfrastructureStack --require-approval never
  exit 0
fi

if [ $app == "app" ]
then
  mvn clean package -f software/pom.xml
  cd infrastructure
  cdk deploy ProductServiceStack --require-approval never
  exit 0
fi
