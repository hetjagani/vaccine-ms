all: clean

clean:
	rm -rf target
	docker kill vms-db
	docker rm vms-db
	rm -rf data

build:
	./mvnw install

database:
	docker run -d --name vms-db -p 3306:3306 -e "MYSQL_ROOT_PASSWORD=mysqlrootpassword" -e "MYSQL_DATABASE=vaccine-ms" -v "data:/var/lib/mysql" mysql

run-be: database build
	java -jar ./target/vms-0.0.1-SNAPSHOT.jar

run: build
	java -jar ./target/vms-0.0.1-SNAPSHOT.jar
