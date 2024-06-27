#!/bin/sh

# Install aws cli
#echo "Installing aws cli..."
#curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "./awscliv2.zip"
#sudo unzip awscliv2.zip
#sudo ./aws/install -i /usr/local/aws-cli -b /usr/local/bin
#
## Download CA from S3
#echo "Downloading certificate from s3..."
#CERT_S3_BUCKET=manulerojas19-terraform-mongo-bucket
#sudo /usr/local/bin/aws s3 cp s3://$CERT_S3_BUCKET/mongoCA.crt .

# Import CA to keystore
echo "Importing certificate..."
keytool -import \
-alias mongo \
-storepass changeit \
-keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts \
-noprompt \
-trustcacerts \
-file ./mongoCA.crt

# Execute app
echo "Running app..."
exec java $JAVA_OPTS -cp @/app/jib-classpath-file @/app/jib-main-class-file